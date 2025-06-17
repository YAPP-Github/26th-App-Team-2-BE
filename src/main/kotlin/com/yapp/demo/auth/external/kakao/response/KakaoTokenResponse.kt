package com.yapp.demo.auth.external.kakao.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Long,
    @JsonProperty("scope")
    val scope: String?,
    @JsonProperty("refresh_token_expires_in")
    val refreshTokenExpiresIn: Long,
)
