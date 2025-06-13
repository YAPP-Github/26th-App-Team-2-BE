package com.yapp.demo.common.security.handler

import com.yapp.demo.common.security.oauth2.userinfo.OAuth2UserInfo
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        requireNotNull(response) { "response is null" }
        requireNotNull(authentication) { "Invalid is null" }

        val authUser = authentication.principal as OAuth2UserInfo

        // TODO(jwt 발급 및 반환)
    }
}
