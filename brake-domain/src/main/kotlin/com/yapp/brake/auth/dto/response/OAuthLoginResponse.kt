package com.yapp.brake.auth.dto.response

data class OAuthLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val memberState: String,
)
