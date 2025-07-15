package com.yapp.brake.group.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class UpdateGroupAppRequest(
    @field:NotNull
    @field:Positive
    val groupAppId: Long,
    @field:NotBlank
    val name: String,
)
