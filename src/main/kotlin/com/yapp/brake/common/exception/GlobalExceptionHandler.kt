package com.yapp.brake.common.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.common.dto.ErrorResponse
import com.yapp.brake.common.dto.FieldErrorResponse
import com.yapp.brake.common.exception.ErrorCode.BAD_REQUEST
import com.yapp.brake.common.exception.ErrorCode.INTERNAL_SERVER_ERROR
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
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val fieldErrors =
            e.bindingResult.fieldErrors.map {
                FieldErrorResponse(
                    field = it.field,
                    reason = it.defaultMessage ?: "Invalid value",
                )
            }

        val errorResponse = ErrorResponse(fieldErrors)
        logger.warn(e) { "code=${BAD_REQUEST.code}, message=$errorResponse" }

        return ResponseEntity.status(BAD_REQUEST.status)
            .body(ApiResponse.error(BAD_REQUEST.status, data = errorResponse, BAD_REQUEST.message, BAD_REQUEST.code))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintException(e: ConstraintViolationException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse =
            e.constraintViolations.map {
                FieldErrorResponse(
                    field = it.propertyPath.toString(),
                    reason = it.message ?: "Invalid value",
                )
            }.let { ErrorResponse(it) }

        logger.warn(e) { "code=${BAD_REQUEST.code}, message=$errorResponse" }

        return ResponseEntity.status(BAD_REQUEST.status)
            .body(ApiResponse.error(BAD_REQUEST.status, data = errorResponse, BAD_REQUEST.message, BAD_REQUEST.code))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMalformedException(e: HttpMessageNotReadableException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val cause = e.cause as? MismatchedInputException

        val errorResponse =
            cause?.path?.mapNotNull {
                FieldErrorResponse(field = it.fieldName)
            }?.let { ErrorResponse(it) }
                ?: ErrorResponse(emptyList())

        return ResponseEntity.status(BAD_REQUEST.status)
            .body(ApiResponse.error(BAD_REQUEST.status, data = errorResponse, BAD_REQUEST.message, BAD_REQUEST.code))
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
