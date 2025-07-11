package com.yapp.brake.auth.service

import com.yapp.brake.auth.dto.request.OAuthLoginRequest
import com.yapp.brake.auth.dto.response.OAuthLoginResponse
import com.yapp.brake.auth.dto.response.RefreshTokenResponse

interface AuthUseCase {
    fun login(request: OAuthLoginRequest): OAuthLoginResponse

    fun refreshToken(refreshToken: String): RefreshTokenResponse

    fun logout(accessToken: String)
}
