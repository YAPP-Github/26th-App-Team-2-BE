package com.yapp.demo.common.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.demo.common.dto.ApiResponse
import com.yapp.demo.common.exception.ErrorCode.UNAUTHORIZED
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class OAuth2LoginFailureHandler(
    private val objectMapper: ObjectMapper,
) : AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?,
    ) {
        requireNotNull(response) { "response is null" }

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
