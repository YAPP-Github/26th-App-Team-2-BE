package com.yapp.brake.oauth.service.apple

import com.yapp.brake.oauth.infrastructure.feign.apple.response.AppleTokenResponse
import com.yapp.brake.oauth.model.OAuthUserInfo
import io.jsonwebtoken.Claims
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppleAuthProviderTest {
    private lateinit var clientSecretGenerator: AppleClientSecretGenerator
    private lateinit var appleTokenProvider: AppleTokenProvider
    private lateinit var appleAuthProvider: AppleAuthProvider

    private val code = "authorizationCode"
    private val clientSecret = "dummy-client-secret"
    private val credential = "refresh-token"
    private val appleTokenResponse =
        AppleTokenResponse(
            accessToken = "access-token",
            expiresIn = 1L,
            idToken = "id-token",
            refreshToken = credential,
            tokenType = "token-type",
        )
    private val mockClaims = mock<Claims>()

    @BeforeEach
    fun setUp() {
        clientSecretGenerator = mock()
        appleTokenProvider = mock()

        appleAuthProvider =
            AppleAuthProvider(
                appleClientSecretGenerator = clientSecretGenerator,
                appleTokenProvider = appleTokenProvider,
            )

        whenever(clientSecretGenerator.getClientSecret())
            .thenReturn(clientSecret)
        whenever(appleTokenProvider.getToken(eq(code), eq(clientSecret)))
            .thenReturn(appleTokenResponse)
        whenever(appleTokenProvider.verifyAndParse(eq(appleTokenResponse.idToken)))
            .thenReturn(mockClaims)
    }

    @Test
    fun `토큰에서 유저 정보를 추출한다`() {
        // given
        whenever(mockClaims["email"]).thenReturn("yappo@breake.com")

        // when
        val userInfo: OAuthUserInfo = appleAuthProvider.getOAuthUserInfo(code)

        // then
        assertEquals(credential, userInfo.credential)
        assertEquals("yappo@breake.com", userInfo.email)
    }

    @Test
    fun `email이 없으면 빈값을 반환한다`() {
        // given
        whenever(mockClaims["email"]).thenReturn(null)

        // when
        val userInfo = appleAuthProvider.getOAuthUserInfo(code)

        // when
        assertEquals(credential, userInfo.credential)
        assertTrue(userInfo.email.isEmpty())
    }
}
