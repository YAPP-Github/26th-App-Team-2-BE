package com.yapp.demo.auth.controller

import com.yapp.demo.auth.dto.request.LogoutRequest
import com.yapp.demo.auth.dto.request.OAuthLoginRequest
import com.yapp.demo.auth.dto.request.OAuthSignOutRequest
import com.yapp.demo.auth.dto.request.RefreshTokenRequest
import com.yapp.demo.auth.dto.response.OAuthLoginResponse
import com.yapp.demo.auth.dto.response.RefreshTokenResponse
import com.yapp.demo.auth.service.AuthUseCase
import com.yapp.demo.common.dto.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authUseCase: AuthUseCase,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody @Valid
        request: OAuthLoginRequest,
    ): ApiResponse<OAuthLoginResponse> {
        return ApiResponse.success(authUseCase.login(request.provider, request.authorizationCode))
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody @Valid
        request: RefreshTokenRequest,
    ): ApiResponse<RefreshTokenResponse> {
        return ApiResponse.success(authUseCase.refreshToken(request.refreshToken))
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(
        @RequestBody @Valid
        request: LogoutRequest,
    ) {
        authUseCase.logout(request.accessToken)
    }

    @PostMapping("/revoke")
    fun revoke(
        @RequestBody @Valid
        request: OAuthSignOutRequest,
    ) {
        authUseCase.signOut(request.provider, request.credential)
    }
}
