package com.yapp.brake.group.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size

data class UpdateGroupAppRequest(
    @field:NotNull
    @field:PositiveOrZero
    val groupAppId: Long,
    @field:NotBlank
    @field:Size(min = 2, max = 10)
    val name: String,
)
