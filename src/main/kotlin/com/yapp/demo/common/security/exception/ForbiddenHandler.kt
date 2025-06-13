package com.yapp.demo.common.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.demo.common.dto.ApiResponse
import com.yapp.demo.common.exception.ErrorCode.FORBIDDEN
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class ForbiddenHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        val apiResponse = ApiResponse.error(FORBIDDEN.status, FORBIDDEN.message)
        val responseBody = objectMapper.writeValueAsString(apiResponse)

        response.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = apiResponse.code
            characterEncoding = "UTF-8"
            writer.write(responseBody)
            writer.flush()
        }
    }
}
