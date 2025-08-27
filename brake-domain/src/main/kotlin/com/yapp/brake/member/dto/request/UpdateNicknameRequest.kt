package com.yapp.brake.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateNicknameRequest(
    @field:NotBlank
    @field:Size(min = 2, max = 10)
    val nickname: String,
)
