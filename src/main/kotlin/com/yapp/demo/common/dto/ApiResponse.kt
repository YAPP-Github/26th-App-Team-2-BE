package com.yapp.demo.common.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val code: Int,
    val data: T? = null,
    val message: String? = null,
) {
    companion object {
        fun <T> success(
            code: Int,
            data: T,
        ): ApiResponse<T> = ApiResponse(code = code, data = data)

        fun error(
            code: Int,
            message: String,
        ): ApiResponse<Unit> = ApiResponse(code = code, message = message)
    }
}
