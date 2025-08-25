package com.yapp.brake.group.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateGroupIosRequest(
    @field:NotBlank
    @field:Size(min = 2, max = 10)
    val name: String,
    @field:NotBlank
    val groupApps: String,
)
