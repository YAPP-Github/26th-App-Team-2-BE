package com.yapp.demo.oauth.service.apple

import com.yapp.demo.oauth.model.OAuthUserInfo
import io.jsonwebtoken.Claims
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppleAuthProviderTest {
    private lateinit var clientSecretGenerator: AppleClientSecretGenerator
    private lateinit var idTokenProvider: AppleIdTokenProvider
    private lateinit var provider: AppleAuthProvider

    @BeforeEach
    fun setUp() {
        clientSecretGenerator = mock()
        idTokenProvider = mock()

        provider =
            AppleAuthProvider(
                appleClientSecretGenerator = clientSecretGenerator,
                appleIdTokenProvider = idTokenProvider,
            )
    }

    @Test
    fun `토큰에서 유저 정보를 추출한다`() {
        // given
        val mockClaims = mock<Claims>()
        whenever(mockClaims.subject).thenReturn("yappo123")
        whenever(mockClaims["email"]).thenReturn("yappo@breake.com")

        whenever(idTokenProvider.verifyAndParse("id-token")).thenReturn(mockClaims)

        // when
        val userInfo: OAuthUserInfo = provider.getUserInfo("id-token")

        // then
        assertEquals("yappo123", userInfo.id)
        assertEquals("yappo@breake.com", userInfo.email)
    }

    @Test
    fun `email이 없으면 빈값을 반환한다`() {
        val mockClaims = mock<Claims>()
        whenever(mockClaims.subject).thenReturn("yappo123")
        whenever(mockClaims["email"]).thenReturn(null)

        whenever(idTokenProvider.verifyAndParse("id-token")).thenReturn(mockClaims)

        val userInfo = provider.getUserInfo("id-token")

        assertEquals("yappo123", userInfo.id)
        assertTrue(userInfo.email.isEmpty())
    }
}
