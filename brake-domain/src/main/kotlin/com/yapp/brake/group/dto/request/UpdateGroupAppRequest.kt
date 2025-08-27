package com.yapp.brake.group.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class UpdateGroupAppRequest(
    @field:Positive
    val groupAppId: Long? = null,
    @field:NotBlank
    val packageName: String,
    @field:NotBlank
    val name: String,
)
