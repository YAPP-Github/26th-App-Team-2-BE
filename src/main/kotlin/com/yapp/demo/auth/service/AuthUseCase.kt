package com.yapp.demo.auth.service

import com.yapp.demo.auth.dto.request.OAuthLoginRequest
import com.yapp.demo.auth.dto.response.OAuthLoginResponse
import com.yapp.demo.auth.dto.response.RefreshTokenResponse

interface AuthUseCase {
    fun login(request: OAuthLoginRequest): OAuthLoginResponse

    fun refreshToken(refreshToken: String): RefreshTokenResponse

    fun logout(accessToken: String)
}
