package com.yapp.brake.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val status: Int,
    val data: T? = null,
    val message: String? = null,
    val code: String? = null,
) {
    companion object {
        fun <T> success(
            status: Int,
            data: T,
        ): ApiResponse<T> = ApiResponse(status = status, data = data)

        fun <T> success(data: T): ApiResponse<T> = ApiResponse(status = HttpStatus.OK.value(), data = data)

        fun error(
            status: Int,
            message: String,
            code: String,
        ): ApiResponse<Unit> = ApiResponse(status = status, message = message, code = code)
    }
}
