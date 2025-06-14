package com.yapp.demo.common.filter

import com.yapp.demo.auth.service.JwtTokenProvider
import com.yapp.demo.common.constants.TOKEN_TYPE_ACCESS
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private val log = KotlinLogging.logger {}

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        return path == "/health"
            || path.startsWith("/oauth2/")
            || path.startsWith("/login/oauth2/")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val accessToken = resolveToken(request)

            val userId = jwtTokenProvider.extractUserId(accessToken, TOKEN_TYPE_ACCESS)

            /***
             * @TODO: UserPrincipal 설계 필요
             * val authorities = listOf(SimpleGrantedAuthority("ROLE_USER")) //
             * val authentication = UsernamePasswordAuthenticationToken(userId, null, authorities)
             * SecurityContextHolder.getContext().authentication = authentication
             */
        } catch (e: Exception) {
            log.warn(e) { "[JwtAuthenticationFilter.doFilterInternal] fail to parse token" }
            throw e
        }

        filterChain.doFilter(request, response)
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
