package com.yapp.demo.auth.service

import com.yapp.demo.auth.dto.request.OAuthLoginRequest
import com.yapp.demo.auth.dto.response.OAuthLoginResponse
import com.yapp.demo.auth.dto.response.RefreshTokenResponse
import com.yapp.demo.auth.infrastructure.BlackListRepository
import com.yapp.demo.auth.infrastructure.RefreshTokenRepository
import com.yapp.demo.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.common.security.getMemberId
import com.yapp.demo.member.infrastructure.MemberReader
import com.yapp.demo.member.infrastructure.MemberWriter
import com.yapp.demo.member.model.Member
import com.yapp.demo.oauth.model.OAuthUserInfo
import com.yapp.demo.oauth.service.OAuthProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

        val authToken = authProvider.getAccessToken(request.authorizationCode)
        val userInfo = authProvider.getUserInfo(authToken)

        val member = findOrCreateMember(request.deviceId, request.provider, userInfo)

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

    override fun withdraw(
        socialProvider: SocialProvider,
        credential: String,
    ) {
        val authProvider =
            findProvider(socialProvider)
                ?: throw CustomException(ErrorCode.BAD_REQUEST)

        authProvider.withdraw(credential)
        memberWriter.delete(getMemberId())
    }

    @Transactional
    fun findOrCreateMember(
        deviceId: String,
        provider: SocialProvider,
        userInfo: OAuthUserInfo,
    ): Member {
        return memberReader.findByDeviceId(deviceId)
            ?: memberWriter.save(
                Member.create(
                    deviceId = deviceId,
                    oAuthUserInfo = userInfo,
                    socialProvider = provider,
                    role = Role.USER,
                ),
            )
    }

    private fun findProvider(socialType: SocialProvider): OAuthProvider? =
        oauthProviders.firstOrNull { it.supports(socialType) }
}
