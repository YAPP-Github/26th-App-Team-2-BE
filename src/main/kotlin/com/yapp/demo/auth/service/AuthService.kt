package com.yapp.demo.auth.service

import com.yapp.demo.auth.dto.response.OAuthLoginResponse
import com.yapp.demo.auth.dto.response.RefreshTokenResponse
import com.yapp.demo.auth.external.OAuthProvider
import com.yapp.demo.auth.infrastructure.BlackListRepository
import com.yapp.demo.auth.infrastructure.RefreshTokenRepository
import com.yapp.demo.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.common.security.getUserId
import com.yapp.demo.user.infrastructure.UserReader
import com.yapp.demo.user.infrastructure.UserWriter
import com.yapp.demo.user.infrastructure.jpa.UserEntity
import com.yapp.demo.user.model.UserStatus
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val oauthProviders: List<OAuthProvider>,
    private val userReader: UserReader,
    private val userWriter: UserWriter,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val blackListRepository: BlackListRepository,
) : AuthUseCase {
    override fun login(
        socialProvider: SocialProvider,
        code: String,
    ): OAuthLoginResponse {
        val authProvider =
            findProvider(socialProvider)
                ?: throw CustomException(ErrorCode.BAD_REQUEST)

        val authToken = authProvider.getAccessToken(code)
        val userInfo = authProvider.getUserInfo(authToken)

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

        val accessToken = jwtTokenProvider.generateAccessToken(user.id)
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.id)
        val ttl = jwtTokenProvider.extractExpiration(refreshToken)

        refreshTokenRepository.add(user.id, refreshToken, Duration.ofMillis(ttl))

        return OAuthLoginResponse(accessToken, refreshToken)
    }

    override fun refreshToken(refreshToken: String): RefreshTokenResponse {
        val userId = jwtTokenProvider.extractUserId(refreshToken, TOKEN_TYPE_REFRESH)
        val user =
            userReader.findById(userId)
                ?: throw CustomException(ErrorCode.USER_NOT_FOUND)

        val savedToken =
            refreshTokenRepository.read(userId)
                ?: throw CustomException(ErrorCode.TOKEN_NOT_FOUND)

        if (savedToken != refreshToken) {
            throw CustomException(ErrorCode.TOKEN_INVALID)
        }

        return RefreshTokenResponse(
            accessToken = jwtTokenProvider.generateAccessToken(user.id),
            refreshToken = jwtTokenProvider.generateRefreshToken(user.id),
        )
    }

    override fun logout(accessToken: String) {
        refreshTokenRepository.remove(getUserId())

        val ttl = jwtTokenProvider.extractExpiration(accessToken)
        blackListRepository.add(accessToken, Duration.ofMillis(ttl))
    }

    private fun findProvider(socialType: SocialProvider): OAuthProvider? =
        oauthProviders.firstOrNull { it.supports(socialType) }
}
