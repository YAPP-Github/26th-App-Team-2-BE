package com.yapp.demo.auth.dto.response

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
