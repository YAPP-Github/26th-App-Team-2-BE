package com.yapp.demo.common.filter

import com.yapp.demo.auth.service.JwtTokenProvider
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
    /**
     * Processes incoming HTTP requests to authenticate users based on a JWT access token.
     *
     * Extracts the JWT access token from the Authorization header, validates its type, and retrieves the user ID.
     * Throws a custom exception if the token is missing, malformed, or of the wrong type.
     * Proceeds with the filter chain after authentication checks.
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val accessToken = resolveToken(request)

            if (!jwtTokenProvider.isAccessToken(accessToken)) {
                throw CustomException(ErrorCode.TOKEN_TYPE_MISMATCH)
            }

            val userId = jwtTokenProvider.extractUserId(accessToken)

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

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request The incoming HTTP request containing the Authorization header.
     * @return The JWT token string without the "Bearer " prefix.
     * @throws CustomException if the Authorization header is missing, empty, or does not start with "Bearer".
     */
    private fun resolveToken(request: HttpServletRequest): String {
        val bearer = request.getHeader("Authorization")

        return if (isValid(bearer)) {
            bearer.substring(7)
        } else {
            throw CustomException(ErrorCode.TOKEN_NOT_FOUND)
        }
    }

    /**
 * Checks if the provided string is a non-empty bearer token starting with "Bearer".
 *
 * @param bearer The authorization header value to validate.
 * @return `true` if the string is non-null, non-empty, and starts with "Bearer"; otherwise, `false`.
 */
private fun isValid(bearer: String?): Boolean = !bearer.isNullOrEmpty() && bearer.startsWith("Bearer")
}
