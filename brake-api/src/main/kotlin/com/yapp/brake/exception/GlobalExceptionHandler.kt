package com.yapp.brake.exception

import com.yapp.brake.dto.ApiResponse
import com.yapp.brake.exception.ErrorCode.BAD_REQUEST
import com.yapp.brake.exception.ErrorCode.INTERNAL_SERVER_ERROR
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ApiResponse<Unit>> {
        logger.warn { "code=${e.errorCode.code}, message=${e.errorCode.message}" }
        return ResponseEntity.status(e.errorCode.status)
            .body(ApiResponse.error(e.errorCode.status, e.errorCode.message, e.errorCode.code))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.warn(e) { "code=${BAD_REQUEST.code}, message=${e.message}" }
        return ResponseEntity.status(BAD_REQUEST.status)
            .body(ApiResponse.error(BAD_REQUEST.status, BAD_REQUEST.message, BAD_REQUEST.code))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        logger.warn(e) { "code=${BAD_REQUEST.code}, message=${e.bindingResult.fieldErrors}" }

        return ResponseEntity.status(BAD_REQUEST.status)
            .body(ApiResponse.error(BAD_REQUEST.status, BAD_REQUEST.message, BAD_REQUEST.code))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintException(e: ConstraintViolationException): ResponseEntity<ApiResponse<Unit>> {
        logger.warn(e) { "code=${BAD_REQUEST.code}, message=${e.constraintViolations}" }

        return ResponseEntity.status(BAD_REQUEST.status)
            .body(ApiResponse.error(BAD_REQUEST.status, BAD_REQUEST.message, BAD_REQUEST.code))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMalformedException(e: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Unit>> {
        logger.warn(e) { "code=${BAD_REQUEST.code}, message=${e.message}" }

        return ResponseEntity.status(BAD_REQUEST.status)
            .body(ApiResponse.error(BAD_REQUEST.status, BAD_REQUEST.message, BAD_REQUEST.code))
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error(e) { "code=${INTERNAL_SERVER_ERROR.code}, message=${e.message}" }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR.status)
            .body(
                ApiResponse.error(
                    INTERNAL_SERVER_ERROR.status,
                    INTERNAL_SERVER_ERROR.message,
                    INTERNAL_SERVER_ERROR.code,
                ),
            )
    }
}
