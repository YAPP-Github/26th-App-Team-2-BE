package com.yapp.brake.common.logging

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Component
class CustomRequestLoggingFilter : CommonsRequestLoggingFilter() {
    init {
        isIncludeQueryString = true
        isIncludePayload = true
        maxPayloadLength = 10_000
        this.setAfterMessagePrefix("[request = ")
    }

    override fun shouldLog(request: HttpServletRequest): Boolean {
        val uri = request.requestURI
        return EXCLUDED_URI_PREFIXES.none { uri.startsWith(it) }
    }

    override fun beforeRequest(
        request: HttpServletRequest,
        message: String,
    ) {
    }

    override fun afterRequest(
        request: HttpServletRequest,
        message: String,
    ) {
        logger.info(message)
    }

    companion object {
        private val EXCLUDED_URI_PREFIXES =
            setOf(
                "/health",
                "/v1/swagger-ui",
                "/static/swagger",
            )
    }
}
