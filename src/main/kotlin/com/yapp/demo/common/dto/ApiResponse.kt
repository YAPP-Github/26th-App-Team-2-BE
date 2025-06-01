package com.yapp.demo.common.dto

data class ApiResponse<T>(
    val data: T,
) {
    companion object {
        fun error(message: String) = ApiResponse(message)
    }
}
