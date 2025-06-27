package com.yapp.demo.auth.service

import com.yapp.demo.auth.dto.response.OAuthLoginResponse
import com.yapp.demo.auth.dto.response.RefreshTokenResponse
import com.yapp.demo.auth.infrastructure.BlackListRepository
import com.yapp.demo.auth.infrastructure.RefreshTokenRepository
import com.yapp.demo.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.common.security.getMemberId
import com.yapp.demo.member.infrastructure.jpa.MemberJpaReader
import com.yapp.demo.member.infrastructure.jpa.MemberJpaWriter
import com.yapp.demo.member.model.Member
import com.yapp.demo.oauth.service.OAuthProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val oauthProviders: List<OAuthProvider>,
    private val memberReader: MemberJpaReader,
    private val memberWriter: MemberJpaWriter,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val blackListRepository: BlackListRepository,
) : AuthUseCase {
    @Transactional
    override fun login(
        socialProvider: SocialProvider,
        code: String,
    ): OAuthLoginResponse {
        val authProvider =
            findProvider(socialProvider)
                ?: throw CustomException(ErrorCode.BAD_REQUEST)

        val authToken = authProvider.getAccessToken(code)
        val userInfo = authProvider.getUserInfo(authToken)

        var member = memberReader.findByAuthEmail(userInfo.email)

        if (member == null) {
            member =
                memberWriter.save(
                    Member.create(
                        authEmail = userInfo.email,
                        socialProvider = socialProvider,
                        role = Role.USER,
                    ),
                )
        }

        val accessToken = jwtTokenProvider.generateAccessToken(member.id)
        val refreshToken = jwtTokenProvider.generateRefreshToken(member.id)
        val ttl = jwtTokenProvider.extractExpiration(refreshToken)

        refreshTokenRepository.add(member.id, refreshToken, Duration.ofMillis(ttl))

        return OAuthLoginResponse(accessToken, refreshToken)
    }

    override fun refreshToken(refreshToken: String): RefreshTokenResponse {
        val memberId = jwtTokenProvider.extractMemberId(refreshToken, TOKEN_TYPE_REFRESH)
        val member = memberReader.getById(memberId)
        val savedToken = refreshTokenRepository.get(memberId)

        if (savedToken != refreshToken) {
            throw CustomException(ErrorCode.TOKEN_INVALID)
        }

        val newAccessToken = jwtTokenProvider.generateAccessToken(member.id)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(member.id)
        val ttl = jwtTokenProvider.extractExpiration(newRefreshToken)

        refreshTokenRepository.add(member.id, newRefreshToken, Duration.ofMillis(ttl))

        return RefreshTokenResponse(newAccessToken, newRefreshToken)
    }

    override fun logout(accessToken: String) {
        refreshTokenRepository.remove(getMemberId())

        val ttl = jwtTokenProvider.extractExpiration(accessToken)
        blackListRepository.add(accessToken, Duration.ofMillis(ttl))
    }

    private fun findProvider(socialType: SocialProvider): OAuthProvider? =
        oauthProviders.firstOrNull { it.supports(socialType) }
}
