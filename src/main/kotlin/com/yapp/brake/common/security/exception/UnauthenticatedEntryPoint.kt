package com.yapp.brake.common.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.common.exception.ErrorCode.UNAUTHORIZED
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class UnauthenticatedEntryPoint(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        val apiResponse = ApiResponse.error(UNAUTHORIZED.status, UNAUTHORIZED.message)
        val responseBody = objectMapper.writeValueAsString(apiResponse)

        response.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = apiResponse.code
            characterEncoding = StandardCharsets.UTF_8.name()
            writer.write(responseBody)
            writer.flush()
        }
    }
}
