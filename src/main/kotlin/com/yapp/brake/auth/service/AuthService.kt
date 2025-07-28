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
import com.yapp.brake.deviceprofile.infrastructure.DeviceProfileReader
import com.yapp.brake.deviceprofile.infrastructure.DeviceProfileWriter
import com.yapp.brake.deviceprofile.model.DeviceProfile
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
    private val deviceProfileReader: DeviceProfileReader,
    private val deviceProfileWriter: DeviceProfileWriter,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val blackListRepository: BlackListRepository,
) : AuthUseCase {
    override fun login(request: OAuthLoginRequest): OAuthLoginResponse {
        val authProvider =
            findProvider(request.provider)
                ?: throw CustomException(ErrorCode.BAD_REQUEST)

        val userInfo = authProvider.getOAuthUserInfo(request.authorizationCode)

        val member = findOrCreateMember(userInfo)
        val deviceProfile = findOrCreateDeviceProfile(member, request.deviceName)

        val accessToken = jwtTokenProvider.generateAccessToken(member.id, deviceProfile.id)
        val refreshToken = jwtTokenProvider.generateRefreshToken(member.id, deviceProfile.id)
        val ttl = jwtTokenProvider.extractExpiration(refreshToken)

        refreshTokenRepository.add(deviceProfile.id, refreshToken, Duration.ofMillis(ttl))

        return OAuthLoginResponse(accessToken, refreshToken, member.state.name)
    }

    override fun refreshToken(refreshToken: String): RefreshTokenResponse {
        val memberId = jwtTokenProvider.extractMemberId(refreshToken, TOKEN_TYPE_REFRESH)
        val deviceProfileId = jwtTokenProvider.extractProfileId(refreshToken)
        val member = memberReader.getById(memberId)
        val savedToken = refreshTokenRepository.get(deviceProfileId)

        if (savedToken != refreshToken) {
            throw CustomException(ErrorCode.TOKEN_INVALID)
        }

        val newAccessToken = jwtTokenProvider.generateAccessToken(member.id, deviceProfileId)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(member.id, deviceProfileId)
        val ttl = jwtTokenProvider.extractExpiration(newRefreshToken)

        refreshTokenRepository.add(deviceProfileId, newRefreshToken, Duration.ofMillis(ttl))

        return RefreshTokenResponse(newAccessToken, newRefreshToken)
    }

    override fun logout(
        deviceProfileId: Long,
        accessToken: String,
    ) {
        refreshTokenRepository.remove(deviceProfileId)

        val ttl = jwtTokenProvider.extractExpiration(accessToken)
        blackListRepository.add(accessToken, Duration.ofMillis(ttl))
    }

    private fun findOrCreateMember(userInfo: OAuthUserInfo): Member {
        return memberReader.findByOauthInfo(userInfo.email, userInfo.socialProvider)
            ?: memberWriter.save(
                Member.create(
                    oAuthUserInfo = userInfo,
                    role = Role.USER,
                ),
            )
    }

    private fun findOrCreateDeviceProfile(
        member: Member,
        deviceName: String,
    ): DeviceProfile {
        return deviceProfileReader.findByMemberIdAndDeviceName(member.id, deviceName)
            ?: deviceProfileWriter.save(
                DeviceProfile.create(
                    memberId = member.id,
                    deviceName = deviceName,
                ),
            )
    }

    private fun findProvider(socialType: SocialProvider): OAuthProvider? =
        oauthProviders.firstOrNull { it.supports(socialType) }
}
