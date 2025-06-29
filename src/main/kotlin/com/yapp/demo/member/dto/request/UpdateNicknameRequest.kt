package com.yapp.demo.member.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdateNicknameRequest(
    @field:NotBlank
    val nickname: String,
)
