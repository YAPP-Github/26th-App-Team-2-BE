package com.yapp.brake.oauth.service.kakao

import com.yapp.brake.oauth.infrastructure.feign.kakao.KakaoApiFeignClient
import com.yapp.brake.oauth.infrastructure.feign.kakao.KakaoAuthFeignClient
import com.yapp.brake.oauth.infrastructure.feign.kakao.response.KakaoTokenResponse
import com.yapp.brake.oauth.infrastructure.feign.kakao.response.KakaoUserInfoResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class KakaoAuthProviderTest {
    private val kakaoProperties = mock<KakaoProperties>()
    private val kakaoAuthFeignClient = mock<KakaoAuthFeignClient>()
    private val kakaoApiFeignClient = mock<KakaoApiFeignClient>()

    private val kakaoAuthProvider =
        KakaoAuthProvider(
            kakaoProperties,
            kakaoAuthFeignClient,
            kakaoApiFeignClient,
        )

    @Test
    fun `유저 정보를 조회한다`() {
        val code = "authorizationCode"
        val tokenResponse =
            KakaoTokenResponse(
                accessToken = "accessToken",
                tokenType = "access",
                refreshToken = "refreshToken",
                expiresIn = 1L,
                scope = null,
                refreshTokenExpiresIn = 2L,
            )
        val expected =
            KakaoUserInfoResponse(
                id = "kakao-id",
                kakaoAccount = KakaoUserInfoResponse.KakaoAccount("kakao@email.com"),
            )

        whenever(
            kakaoAuthFeignClient.getToken(
                "authorization_code",
                kakaoProperties.clientId,
                kakaoProperties.redirectUri,
                code,
            ),
        ).thenReturn(tokenResponse)
        whenever(kakaoApiFeignClient.getUserInfo(any())).thenReturn(expected)

        val result = kakaoAuthProvider.getOAuthUserInfo(code)

        assertThat(result.credential).isEqualTo(expected.id)
        assertThat(result.email).isEqualTo(expected.kakaoAccount.email)
    }
}
