package com.yapp.brake.group.dto.request

import jakarta.validation.constraints.NotBlank

data class AddGroupAppRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val packageName: String,
)
