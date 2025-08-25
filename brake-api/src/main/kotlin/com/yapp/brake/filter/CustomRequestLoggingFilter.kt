package com.yapp.brake.filter

import com.yapp.brake.constants.ALLOWED_URIS
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
        return ALLOWED_URIS.none { uri.startsWith(it) }
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
}
