package com.yapp.brake.auth.service

import com.yapp.brake.auth.dto.request.OAuthLoginRequest
import com.yapp.brake.auth.dto.response.OAuthLoginResponse
import com.yapp.brake.auth.dto.response.RefreshTokenResponse
import com.yapp.brake.auth.infrastructure.BlackListRepository
import com.yapp.brake.auth.infrastructure.RefreshTokenRepository
import com.yapp.brake.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.brake.common.enums.Role
import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.common.security.getMemberId
import com.yapp.brake.member.infrastructure.MemberReader
import com.yapp.brake.member.infrastructure.MemberWriter
import com.yapp.brake.member.model.Member
import com.yapp.brake.oauth.model.OAuthUserInfo
import com.yapp.brake.oauth.service.OAuthProvider
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val oauthProviders: List<OAuthProvider>,
    private val memberReader: MemberReader,
    private val memberWriter: MemberWriter,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val blackListRepository: BlackListRepository,
) : AuthUseCase {
    override fun login(request: OAuthLoginRequest): OAuthLoginResponse {
        val authProvider =
            findProvider(request.provider)
                ?: throw CustomException(ErrorCode.BAD_REQUEST)

        val userInfo = authProvider.getOAuthUserInfo(request.authorizationCode)

        val member = findOrCreateMember(request.deviceId, userInfo)

        val accessToken = jwtTokenProvider.generateAccessToken(member.id)
        val refreshToken = jwtTokenProvider.generateRefreshToken(member.id)
        val ttl = jwtTokenProvider.extractExpiration(refreshToken)

        refreshTokenRepository.add(member.id, refreshToken, Duration.ofMillis(ttl))

        return OAuthLoginResponse(accessToken, refreshToken, member.state.name)
    }

    override fun refreshToken(refreshToken: String): RefreshTokenResponse {
        val memberId = jwtTokenProvider.extractMemberId(refreshToken, TOKEN_TYPE_REFRESH)
        val member = memberReader.getById(memberId)
        val savedToken = refreshTokenRepository.get(memberId)

        if (savedToken != refreshToken) {
            throw CustomException(ErrorCode.TOKEN_INVALID)
        }

        val newAccessToken = jwtTokenProvider.generateAccessToken(member.id)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(member.id)
        val ttl = jwtTokenProvider.extractExpiration(newRefreshToken)

        refreshTokenRepository.add(member.id, newRefreshToken, Duration.ofMillis(ttl))

        return RefreshTokenResponse(newAccessToken, newRefreshToken)
    }

    override fun logout(accessToken: String) {
        refreshTokenRepository.remove(getMemberId())

        val ttl = jwtTokenProvider.extractExpiration(accessToken)
        blackListRepository.add(accessToken, Duration.ofMillis(ttl))
    }

    override fun withdraw(socialProvider: SocialProvider) {
        val authProvider =
            findProvider(socialProvider)
                ?: throw CustomException(ErrorCode.BAD_REQUEST)

        val member = memberReader.getById(getMemberId())
        authProvider.withdraw(member.oAuthUserInfo)

        memberWriter.delete(getMemberId())
    }

    private fun findOrCreateMember(
        deviceId: String,
        userInfo: OAuthUserInfo,
    ): Member {
        return memberReader.findByDeviceId(deviceId)
            ?: memberWriter.save(
                Member.create(
                    deviceId = deviceId,
                    oAuthUserInfo = userInfo,
                    role = Role.USER,
                ),
            )
    }

    private fun findProvider(socialType: SocialProvider): OAuthProvider? =
        oauthProviders.firstOrNull { it.supports(socialType) }
}
