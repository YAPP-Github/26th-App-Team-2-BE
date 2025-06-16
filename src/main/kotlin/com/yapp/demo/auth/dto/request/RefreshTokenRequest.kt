package com.yapp.demo.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank(message = "리프레시 토큰은 필수입니다.")
    val refreshToken: String,
)
