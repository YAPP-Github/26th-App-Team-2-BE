package com.yapp.brake.auth.service

import com.yapp.brake.auth.dto.request.OAuthLoginRequest
import com.yapp.brake.auth.infrastructure.BlackListRepository
import com.yapp.brake.auth.infrastructure.RefreshTokenRepository
import com.yapp.brake.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.deviceprofile.infrastructure.DeviceProfileReader
import com.yapp.brake.deviceprofile.infrastructure.DeviceProfileWriter
import com.yapp.brake.member.infrastructure.jpa.MemberJpaReader
import com.yapp.brake.member.infrastructure.jpa.MemberJpaWriter
import com.yapp.brake.oauth.model.OAuthUserInfo
import com.yapp.brake.oauth.service.OAuthProvider
import com.yapp.brake.support.fixture.model.deviceProfileFixture
import com.yapp.brake.support.fixture.model.memberFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Duration

class AuthServiceTest {
    private val userInfo = OAuthUserInfo(SocialProvider.KAKAO, "id", "test@email.com")
    private val code = "authCode"
    private val mockProvider =
        mock<OAuthProvider> {
            on { supports(SocialProvider.KAKAO) } doReturn true
            on { getOAuthUserInfo(code) } doReturn userInfo
        }

    private val oauthProviders = listOf(mockProvider)
    private val jwtTokenProvider = mock<JwtTokenProvider>()
    private val memberReader = mock<MemberJpaReader>()
    private val memberWriter = mock<MemberJpaWriter>()
    private val deviceProfileReader = mock<DeviceProfileReader>()
    private val deviceProfileWriter = mock<DeviceProfileWriter>()
    private val refreshTokenRepository = mock<RefreshTokenRepository>()
    private val blackListRepository = mock<BlackListRepository>()

    private val authService =
        AuthService(
            jwtTokenProvider,
            oauthProviders,
            memberReader,
            memberWriter,
            deviceProfileReader,
            deviceProfileWriter,
            refreshTokenRepository,
            blackListRepository,
        )

    @Test
    fun `login 새로은 토큰을 발급한다`() {
        // given
        val provider = SocialProvider.KAKAO
        val accessToken = "access-token"
        val refreshToken = "refresh-token"

        val member = memberFixture(id = 1L, oAuthUserInfo = userInfo)
        val deviceProfile = deviceProfileFixture()

        val request =
            OAuthLoginRequest(
                provider = provider,
                authorizationCode = code,
                deviceId = deviceProfile.deviceName,
            )

        whenever(memberReader.findByOauthInfo(userInfo.email, userInfo.socialProvider)).thenReturn(member)
        whenever(
            deviceProfileReader.findByMemberIdAndDeviceName(
                member.id,
                deviceProfile.deviceName,
            ),
        ).thenReturn(deviceProfile)

        whenever(jwtTokenProvider.generateAccessToken(member.id, deviceProfile.id)).thenReturn(accessToken)
        whenever(jwtTokenProvider.generateRefreshToken(member.id, deviceProfile.id)).thenReturn(refreshToken)
        whenever(jwtTokenProvider.extractExpiration(refreshToken)).thenReturn(60000L)

        // when
        val result = authService.login(request)

        // then
        assertThat(result.accessToken).isEqualTo(accessToken)
        assertThat(result.refreshToken).isEqualTo(refreshToken)
    }

    @Nested
    inner class RefreshTokenTest {
        private val refreshToken = "refresh-token"
        private val memberId = 1L
        private val member = memberFixture(id = 1, oAuthUserInfo = userInfo)
        private val deviceProfile = deviceProfileFixture()

        @Test
        fun `refresh는 새로운 리프레시 토큰과 액세스 토큰을 발급한다`() {
            val newAccessToken = "new-access-token"
            val newRefreshToken = "new-refresh-token"

            whenever(jwtTokenProvider.extractMemberId(refreshToken, TOKEN_TYPE_REFRESH)).thenReturn(member.id)
            whenever(jwtTokenProvider.extractProfileId(refreshToken)).thenReturn(deviceProfile.id)
            whenever(memberReader.getById(member.id)).thenReturn(member)
            whenever(refreshTokenRepository.get(member.id)).thenReturn(refreshToken)
            whenever(jwtTokenProvider.generateRefreshToken(member.id, deviceProfile.id)).thenReturn(newRefreshToken)
            whenever(jwtTokenProvider.generateAccessToken(member.id, deviceProfile.id)).thenReturn(newAccessToken)

            val result = authService.refreshToken(refreshToken)

            assertThat(result.accessToken).isEqualTo(newAccessToken)
            assertThat(result.refreshToken).isEqualTo(newRefreshToken)
        }

        @Test
        fun `refresh는 유저가 존재하지 않으면 예외를 던진다`() {
            whenever(jwtTokenProvider.extractMemberId(refreshToken, TOKEN_TYPE_REFRESH)).thenReturn(memberId)
            whenever(jwtTokenProvider.extractProfileId(refreshToken)).thenReturn(deviceProfile.id)
            whenever(memberReader.getById(memberId)).thenThrow(CustomException(ErrorCode.MEMBER_NOT_FOUND))

            val exception =
                assertThrows<CustomException> {
                    authService.refreshToken(refreshToken)
                }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.MEMBER_NOT_FOUND)
        }

        @Test
        fun `refresh는 기존 토큰이 저장소에 존재하지 않으면 예외를 던진다`() {
            whenever(jwtTokenProvider.extractMemberId(refreshToken, TOKEN_TYPE_REFRESH)).thenReturn(member.id)
            whenever(jwtTokenProvider.extractProfileId(refreshToken)).thenReturn(deviceProfile.id)
            whenever(memberReader.getById(member.id)).thenReturn(member)
            whenever(refreshTokenRepository.get(member.id)).thenThrow(CustomException(ErrorCode.TOKEN_NOT_FOUND))

            val exception =
                assertThrows<CustomException> {
                    authService.refreshToken(refreshToken)
                }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.TOKEN_NOT_FOUND)
        }

        @Test
        fun `refresh는 기존 토큰이 저장소의 토큰과 일치하지 않으면 예외를 던진다`() {
            whenever(jwtTokenProvider.extractMemberId(refreshToken, TOKEN_TYPE_REFRESH)).thenReturn(member.id)
            whenever(jwtTokenProvider.extractProfileId(refreshToken)).thenReturn(deviceProfile.id)
            whenever(memberReader.getById(member.id)).thenReturn(member)
            whenever(refreshTokenRepository.get(member.id)).thenReturn("another-token")

            val exception =
                assertThrows<CustomException> {
                    authService.refreshToken(refreshToken)
                }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.TOKEN_INVALID)
        }
    }

    @Test
    fun `logout은 유저의 토큰을 삭제하고 블랙리스트에 추가한다`() {
        val accessToken = "access-token"
        val ttl = 12345L
        val memberId = 1L
        val deviceProfileId = 1L

        // SecurityContext 설정
        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), deviceProfileId)
        SecurityContextHolder.getContext().authentication = authentication

        whenever(jwtTokenProvider.extractExpiration(accessToken)).thenReturn(ttl)
        // when
        authService.logout(accessToken)

        // then
        verify(refreshTokenRepository).remove(memberId)
        verify(blackListRepository).add(accessToken, Duration.ofMillis(ttl))

        SecurityContextHolder.clearContext()
    }
}
