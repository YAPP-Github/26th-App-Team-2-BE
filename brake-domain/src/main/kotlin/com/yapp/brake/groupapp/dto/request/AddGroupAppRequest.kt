package com.yapp.brake.groupapp.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class AddGroupAppRequest(
    @field:NotNull
    @field:Positive
    val groupId: Long,
    @field:NotBlank
    val appId: String,
)
