package com.yapp.brake.auth.controller

import com.yapp.brake.auth.dto.request.OAuthLoginRequest
import com.yapp.brake.auth.dto.request.RefreshTokenRequest
import com.yapp.brake.auth.dto.response.OAuthLoginResponse
import com.yapp.brake.auth.dto.response.RefreshTokenResponse
import com.yapp.brake.auth.service.AuthUseCase
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.common.security.getDeviceProfileId
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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
        return ApiResponse.success(authUseCase.login(request))
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
        @RequestHeader("Authorization") authorizationHeader: String,
    ) {
        val accessToken = authorizationHeader.removePrefix("Bearer ").trim()
        authUseCase.logout(getDeviceProfileId(), accessToken)
    }
}
