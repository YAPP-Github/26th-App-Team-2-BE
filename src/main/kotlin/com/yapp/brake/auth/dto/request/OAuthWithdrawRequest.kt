package com.yapp.brake.auth.dto.request

import com.yapp.brake.common.enums.SocialProvider
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotNull

data class OAuthWithdrawRequest(
    @field:NotNull(message = "소셜 로그인 타입은 필수 입니다.")
    val provider: SocialProvider,
    @field:Nullable
    val authorizationCode: String? = null,
)
