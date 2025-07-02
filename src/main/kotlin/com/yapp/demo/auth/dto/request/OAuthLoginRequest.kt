package com.yapp.demo.auth.dto.request

import com.yapp.demo.common.enums.SocialProvider
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OAuthLoginRequest(
    @field:NotNull(message = "소셜 로그인 타입은 필수 입니다.")
    val provider: SocialProvider,
    @field:NotBlank(message = "인가 코드는 필수 입니다.")
    val authorizationCode: String,
    @field:NotBlank(message = "디바이스 식별자는 필수 입니다.")
    val deviceId: String,
)
