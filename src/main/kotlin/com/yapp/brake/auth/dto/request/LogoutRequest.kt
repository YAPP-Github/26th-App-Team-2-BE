package com.yapp.brake.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class LogoutRequest(
    @field:NotBlank(message = "액세스 토큰은 필수입니다.")
    val accessToken: String,
)
