package com.yapp.brake.oauth.feign.kakao.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUserInfoResponse(
    val id: String,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount,
) {
    data class KakaoAccount(
        @JsonProperty("email")
        val email: String,
    )

    companion object {
        fun createEmpty() =
            KakaoUserInfoResponse(
                id = "",
                kakaoAccount = KakaoAccount(""),
            )
    }
}
