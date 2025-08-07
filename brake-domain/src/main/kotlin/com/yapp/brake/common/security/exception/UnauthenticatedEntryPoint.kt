package com.yapp.brake.common.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class UnauthenticatedEntryPoint(
    private val objectMapper: ObjectMapper,
    @Qualifier("handlerExceptionResolver")
    private val resolver: HandlerExceptionResolver,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        val exception = request.getAttribute(ATTRIBUTE_EXCEPTION_KEY) as Exception
        resolver.resolveException(request, response, null, exception)
    }

    companion object {
        const val ATTRIBUTE_EXCEPTION_KEY = "exception"
    }
}
