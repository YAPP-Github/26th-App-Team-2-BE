package com.yapp.brake.common.filter

import com.yapp.brake.auth.infrastructure.redis.RedisBlackListRepository
import com.yapp.brake.auth.service.JwtTokenProvider
import com.yapp.brake.common.constants.TOKEN_TYPE_ACCESS
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

private val log = KotlinLogging.logger {}

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val blackListRepository: RedisBlackListRepository,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val accessToken = resolveToken(request)
            val memberId = jwtTokenProvider.extractMemberId(accessToken, TOKEN_TYPE_ACCESS)

            if (isBlackList(accessToken)) {
                throw CustomException(ErrorCode.FORBIDDEN)
            }

            val authentication = jwtTokenProvider.getAuthentication(memberId)

            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            log.warn(e) { "[JwtAuthenticationFilter.doFilterInternal] fail to parse token" }
            throw e
        }

        filterChain.doFilter(request, response)
    }

    private fun isBlackList(token: String): Boolean {
        val blacklistToken =
            blackListRepository.read(token)
                ?: return false

        return blacklistToken == token
    }

    private fun resolveToken(request: HttpServletRequest): String {
        val bearer = request.getHeader("Authorization")

        return if (isValid(bearer)) {
            bearer.substringAfter("Bearer ").trim()
        } else {
            throw CustomException(ErrorCode.TOKEN_NOT_FOUND)
        }
    }

    private fun isValid(bearer: String?): Boolean = !bearer.isNullOrEmpty() && bearer.startsWith("Bearer ")
}
