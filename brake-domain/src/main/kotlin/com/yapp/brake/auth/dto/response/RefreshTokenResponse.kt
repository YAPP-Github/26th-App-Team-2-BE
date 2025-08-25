package com.yapp.brake.auth.dto.response

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
