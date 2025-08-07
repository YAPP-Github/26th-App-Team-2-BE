package com.yapp.brake.group.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateGroupRequest(
    @field:NotBlank
    @field:Size(min = 2, max = 10)
    val name: String,
    @field:Valid
    val groupApps: List<UpdateGroupAppRequest> = emptyList(),
)
