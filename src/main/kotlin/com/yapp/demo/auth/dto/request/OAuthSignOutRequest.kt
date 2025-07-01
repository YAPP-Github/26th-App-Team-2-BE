package com.yapp.demo.auth.dto.request

import com.yapp.demo.common.enums.SocialProvider
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OAuthSignOutRequest(
    @field:NotNull(message = "소셜 로그인 타입은 필수 입니다.")
    val provider: SocialProvider,
    @field:NotBlank(message = "인가 정보는 필수 입니다.")
    val credential: String,
)
