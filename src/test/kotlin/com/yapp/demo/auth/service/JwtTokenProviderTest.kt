package com.yapp.demo.auth.service

import com.yapp.demo.common.config.properties.JwtProperties
import com.yapp.demo.common.constants.TOKEN_TYPE_ACCESS
import com.yapp.demo.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test

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

    @BeforeEach
    fun setUp() {
        jwtTokenProvider = JwtTokenProvider(jwtProperties)
    }

    @Test
    fun `AccessToken과 RefreshToken 생성 후 userId 추출이 동일해야 한다`() {
        val userId = "12345"

        val accessToken = jwtTokenProvider.generateAccessToken(userId)
        val refreshToken = jwtTokenProvider.generateRefreshToken(userId)

        val extractedUserIdFromAccess = jwtTokenProvider.extractUserId(accessToken, TOKEN_TYPE_ACCESS)
        val extractedUserIdFromRefresh = jwtTokenProvider.extractUserId(refreshToken, TOKEN_TYPE_REFRESH)

        assertEquals(userId.toLong(), extractedUserIdFromAccess)
        assertEquals(userId.toLong(), extractedUserIdFromRefresh)
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

        val expiredTokenProvider = JwtTokenProvider(expiredJwtProperties)
        val token = expiredTokenProvider.generateAccessToken("1")

        val exception =
            assertThrows<CustomException> {
                expiredTokenProvider.extractUserId(token, TOKEN_TYPE_ACCESS)
            }

        assertEquals(ErrorCode.TOKEN_EXPIRED, exception.errorCode)
    }
}
