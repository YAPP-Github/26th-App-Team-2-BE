package com.yapp.demo.auth.service

import com.yapp.demo.auth.external.OAuthProvider
import com.yapp.demo.auth.external.OAuthUserInfo
import com.yapp.demo.auth.infrastructure.BlackListRepository
import com.yapp.demo.auth.infrastructure.RefreshTokenRepository
import com.yapp.demo.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.user.infrastructure.UserReader
import com.yapp.demo.user.infrastructure.UserWriter
import com.yapp.demo.user.model.User
import com.yapp.demo.user.model.UserStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Duration

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {
    private val userInfo = OAuthUserInfo("id", "test@email.com")
    private val code = "authCode"
    private val mockProvider =
        mock<OAuthProvider> {
            on { supports(SocialProvider.KAKAO) } doReturn true
            on { getAccessToken(code) } doReturn "token"
            on { getUserInfo("token") } doReturn userInfo
        }

    private val oauthProviders = listOf(mockProvider)
    private val jwtTokenProvider = mock<JwtTokenProvider>()
    private val userReader = mock<UserReader>()
    private val userWriter = mock<UserWriter>()
    private val refreshTokenRepository = mock<RefreshTokenRepository>()
    private val blackListRepository = mock<BlackListRepository>()

    private val authService =
        AuthService(
            jwtTokenProvider,
            oauthProviders,
            userReader,
            userWriter,
            refreshTokenRepository,
            blackListRepository,
        )

    @Test
    fun `login 새로은 토큰을 발급한다`() {
        // given
        val provider = SocialProvider.KAKAO
        val accessToken = "access-token"
        val refreshToken = "refresh-token"
        val user =
            User(
                id = 1L,
                authEmail = userInfo.email,
                socialProvider = provider,
                role = Role.USER,
                status = UserStatus.ACTIVE,
            )

        whenever(userReader.findByAuthEmail(userInfo.email)).thenReturn(user)

        whenever(jwtTokenProvider.generateAccessToken(user.id)).thenReturn(accessToken)
        whenever(jwtTokenProvider.generateRefreshToken(user.id)).thenReturn(refreshToken)
        whenever(jwtTokenProvider.extractExpiration(refreshToken)).thenReturn(60000L)

        // when
        val result = authService.login(provider, code)

        // then
        assertThat(result.accessToken).isEqualTo(accessToken)
        assertThat(result.refreshToken).isEqualTo(refreshToken)
    }

    @Nested
    inner class RefreshTokenTest {
        private val refreshToken = "refresh-token"
        private val userId = 1L
        private val user =
            User(
                id = userId,
                authEmail = userInfo.email,
                socialProvider = SocialProvider.KAKAO,
                role = Role.USER,
                status = UserStatus.ACTIVE,
            )

        @Test
        fun `refresh는 새로운 리프레시 토큰과 액세스 토큰을 발급한다`() {
            val newAccessToken = "new-access-token"
            val newRefreshToken = "new-refresh-token"

            `when`(jwtTokenProvider.extractUserId(refreshToken, TOKEN_TYPE_REFRESH)).thenReturn(user.id)
            `when`(userReader.getById(user.id)).thenReturn(user)
            `when`(refreshTokenRepository.get(user.id)).thenReturn(refreshToken)
            `when`(jwtTokenProvider.generateRefreshToken(user.id)).thenReturn(newRefreshToken)
            `when`(jwtTokenProvider.generateAccessToken(user.id)).thenReturn(newAccessToken)

            val result = authService.refreshToken(refreshToken)

            assertThat(result.accessToken).isEqualTo(newAccessToken)
            assertThat(result.refreshToken).isEqualTo(newRefreshToken)
        }

        @Test
        fun `refresh는 유저가 존재하지 않으면 예외를 던진다`() {
            `when`(jwtTokenProvider.extractUserId(refreshToken, TOKEN_TYPE_REFRESH)).thenReturn(userId)
            `when`(userReader.getById(userId)).thenThrow(CustomException(ErrorCode.USER_NOT_FOUND))

            val exception =
                assertThrows<CustomException> {
                    authService.refreshToken(refreshToken)
                }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
        }

        @Test
        fun `refresh는 기존 토큰이 저장소에 존재하지 않으면 예외를 던진다`() {
            `when`(jwtTokenProvider.extractUserId(refreshToken, TOKEN_TYPE_REFRESH)).thenReturn(user.id)
            `when`(userReader.getById(user.id)).thenReturn(user)
            `when`(refreshTokenRepository.get(user.id)).thenThrow(CustomException(ErrorCode.TOKEN_NOT_FOUND))

            val exception =
                assertThrows<CustomException> {
                    authService.refreshToken(refreshToken)
                }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.TOKEN_NOT_FOUND)
        }

        @Test
        fun `refresh는 기존 토큰이 저장소의 토큰과 일치하지 않으면 예외를 던진다`() {
            `when`(jwtTokenProvider.extractUserId(refreshToken, TOKEN_TYPE_REFRESH)).thenReturn(user.id)
            `when`(userReader.getById(user.id)).thenReturn(user)
            `when`(refreshTokenRepository.get(user.id)).thenReturn("another-token")

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
        val userId = 1L

        // SecurityContext 설정
        val authentication = UsernamePasswordAuthenticationToken(userId.toString(), null)
        SecurityContextHolder.getContext().authentication = authentication

        `when`(jwtTokenProvider.extractExpiration(accessToken)).thenReturn(ttl)
        // when
        authService.logout(accessToken)

        // then
        verify(refreshTokenRepository).remove(userId)
        verify(blackListRepository).add(accessToken, Duration.ofMillis(ttl))

        SecurityContextHolder.clearContext()
    }
}
