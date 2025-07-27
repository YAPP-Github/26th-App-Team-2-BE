package com.yapp.brake.common.dto

import com.fasterxml.jackson.annotation.JsonInclude

data class ErrorResponse(
    val errors: List<FieldErrorResponse>,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FieldErrorResponse(
    val field: String,
    val reason: String? = null,
)
