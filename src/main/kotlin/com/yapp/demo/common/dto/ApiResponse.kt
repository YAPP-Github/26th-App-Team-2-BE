package com.yapp.demo.common.dto

data class ApiResponse<T>(
    val code: Int,
    val data: T,
) {
    companion object {
        fun <T> success(
            code: Int,
            data: T,
        ) = ApiResponse(code, data)

        fun error(
            code: Int,
            message: String,
        ) = ApiResponse(code, message)
    }
}
