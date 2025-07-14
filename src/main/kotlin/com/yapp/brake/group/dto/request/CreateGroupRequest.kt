package com.yapp.brake.group.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateGroupRequest(
    @NotBlank
    @Size(min = 2, max = 30)
    val name: String,
)
