package com.yapp.demo.auth.controller

import com.yapp.demo.auth.dto.request.OAuthLoginRequest
import com.yapp.demo.auth.dto.request.RefreshTokenRequest
import com.yapp.demo.auth.dto.response.OAuthLoginResponse
import com.yapp.demo.auth.dto.response.RefreshTokenResponse
import com.yapp.demo.auth.service.AuthService
import com.yapp.demo.common.dto.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody request: OAuthLoginRequest,
    ): ApiResponse<OAuthLoginResponse> {
        return ApiResponse.success(authService.login(request.provider, request.authorizationCode))
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody request: RefreshTokenRequest,
    ): ApiResponse<RefreshTokenResponse> {
        return ApiResponse.success(authService.refreshToken(request.refreshToken))
    }
}
