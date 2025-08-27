package com.yapp.brake.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.brake.common.exception.ErrorCode.FORBIDDEN
import com.yapp.brake.dto.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class ForbiddenHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        val apiResponse = ApiResponse.error(FORBIDDEN.status, FORBIDDEN.message, FORBIDDEN.code)
        val responseBody = objectMapper.writeValueAsString(apiResponse)

        response.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = apiResponse.status
            characterEncoding = StandardCharsets.UTF_8.name()
            writer.write(responseBody)
            writer.flush()
        }
    }
}
