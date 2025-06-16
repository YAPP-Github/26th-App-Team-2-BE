package com.yapp.demo.auth.dto.response

data class OAuthLoginResponse(
    val accessToken: String,
    val refreshToken: String,
)
