package com.yapp.demo.auth.service

import com.yapp.demo.common.config.properties.JwtProperties
import com.yapp.demo.common.constants.TOKEN_TYPE_ACCESS
import com.yapp.demo.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.user.infrastructure.UserReader
import com.yapp.demo.user.model.User
import com.yapp.demo.user.model.UserStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.springframework.security.core.authority.SimpleGrantedAuthority
import kotlin.test.Test
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class JwtTokenProviderTest {
    private val secretKey = "mysecretkey123456789012345678901234mysecretkey123456789012345678901234"
    private val jwtProperties =
        JwtProperties(
            secret = secretKey,
            accessTokenExpiryTime = 3600,
            refreshTokenExpiryTime = 604800,
        )

    private lateinit var jwtTokenProvider: JwtTokenProvider

    private val userReader = mock<UserReader>()

    @BeforeEach
    fun setUp() {
        jwtTokenProvider = JwtTokenProvider(jwtProperties, userReader)
    }

    @Test
    fun `AccessToken과 RefreshToken 생성 후 userId 추출이 동일해야 한다`() {
        val userId = 12345L

        val accessToken = jwtTokenProvider.generateAccessToken(userId)
        val refreshToken = jwtTokenProvider.generateRefreshToken(userId)

        val extractedUserIdFromAccess = jwtTokenProvider.extractUserId(accessToken, TOKEN_TYPE_ACCESS)
        val extractedUserIdFromRefresh = jwtTokenProvider.extractUserId(refreshToken, TOKEN_TYPE_REFRESH)

        assertEquals(userId, extractedUserIdFromAccess)
        assertEquals(userId, extractedUserIdFromRefresh)
    }

    @Test
    fun `잘못된 토큰을 넣으면 TOKEN_INVALID 예외가 발생해야 한다`() {
        val invalidToken = "this.is.not.a.valid.token"

        val exception =
            assertThrows<CustomException> {
                jwtTokenProvider.extractUserId(invalidToken, TOKEN_TYPE_ACCESS)
            }

        assertEquals(ErrorCode.TOKEN_INVALID, exception.errorCode)
    }

    @Test
    fun `만료된 토큰을 넣으면 TOKEN_EXPIRED 예외가 발생해야 한다`() {
        val expiredJwtProperties =
            JwtProperties(
                secret = secretKey,
                accessTokenExpiryTime = -1,
                refreshTokenExpiryTime = -1,
            )

        val expiredTokenProvider = JwtTokenProvider(expiredJwtProperties, userReader)
        val token = expiredTokenProvider.generateAccessToken(1L)

        val exception =
            assertThrows<CustomException> {
                expiredTokenProvider.extractUserId(token, TOKEN_TYPE_ACCESS)
            }

        assertEquals(ErrorCode.TOKEN_EXPIRED, exception.errorCode)
    }

    @Test
    fun `getAuthentication은 유효한 userId에 대해 Authentication 객체를 반환해야 한다`() {
        // given
        val userId = 123L
        val user =
            User(
                id = userId,
                authEmail = "email@email.com",
                socialProvider = SocialProvider.KAKAO,
                role = Role.USER,
                status = UserStatus.ACTIVE,
            )

        `when`(userReader.findById(userId)).thenReturn(user)

        // when
        val authentication = jwtTokenProvider.getAuthentication(userId)

        // then
        assertEquals(userId, authentication.principal)
        assertTrue(authentication.authorities.contains(SimpleGrantedAuthority(Role.USER.name)))
        assertEquals(null, authentication.credentials)
    }

    @Test
    fun `JwtTokenProvider는 토큰으로 부터 만료시간을 추출할 수 있다`() {
        val userId = 12345L

        val accessToken = jwtTokenProvider.generateAccessToken(userId)
        val expiration = jwtTokenProvider.extractExpiration(accessToken)

        val now = System.currentTimeMillis()
        val expiryWithinRange = expiration in (now + 1000)..(now + jwtProperties.accessTokenExpiryTime * 1000)

        assertEquals(true, expiryWithinRange)
    }
}
