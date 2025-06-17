package com.yapp.demo.auth.service

import com.yapp.demo.auth.dto.response.OAuthLoginResponse
import com.yapp.demo.auth.dto.response.RefreshTokenResponse
import com.yapp.demo.auth.external.OAuthProvider
import com.yapp.demo.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.user.infrastructure.UserReader
import com.yapp.demo.user.infrastructure.UserWriter
import com.yapp.demo.user.infrastructure.jpa.UserEntity
import com.yapp.demo.user.model.UserStatus
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val oauthProviders: List<OAuthProvider>,
    private val userReader: UserReader,
    private val userWriter: UserWriter,
) {
    fun login(
        socialProvider: SocialProvider,
        code: String,
    ): OAuthLoginResponse {
        val authProvider =
            findProvider(socialProvider)
                ?: throw CustomException(ErrorCode.BAD_REQUEST)

        val accessToken = authProvider.getAccessToken(code)
        val userInfo = authProvider.getUserInfo(accessToken)

        var user = userReader.findByAuthEmail(userInfo.email)

        if (user == null) {
            user =
                userWriter.save(
                    UserEntity(
                        authEmail = userInfo.email,
                        socialProvider = socialProvider,
                        role = Role.USER,
                        status = UserStatus.ACTIVE,
                    ),
                )
        }

        // 토큰 redis 저장 처리

        return OAuthLoginResponse(
            accessToken = jwtTokenProvider.generateAccessToken(user.id),
            refreshToken = jwtTokenProvider.generateRefreshToken(user.id),
        )
    }

    fun refreshToken(refreshToken: String): RefreshTokenResponse {
        val userId = jwtTokenProvider.extractUserId(refreshToken, TOKEN_TYPE_REFRESH)
        val user =
            userReader.findById(userId)
                ?: throw CustomException(ErrorCode.USER_NOT_FOUND)

        // 토른 레디스 조회 및 유효성 검사

        // 토큰 레디스 다시 저장

        return RefreshTokenResponse(
            accessToken = jwtTokenProvider.generateRefreshToken(user.id),
            refreshToken = jwtTokenProvider.generateRefreshToken(user.id),
        )
    }

    private fun findProvider(socialType: SocialProvider): OAuthProvider? =
        oauthProviders.firstOrNull { it.supports(socialType) }
}
