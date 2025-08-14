package com.yapp.brake.common.filter

import com.yapp.brake.auth.infrastructure.BlackListRepository
import com.yapp.brake.auth.infrastructure.TokenProvider
import com.yapp.brake.common.constants.TOKEN_TYPE_ACCESS
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.common.security.exception.UnauthenticatedEntryPoint.Companion.ATTRIBUTE_EXCEPTION_KEY
import com.yapp.brake.deviceprofile.infrastructure.DeviceProfileReader
import com.yapp.brake.member.infrastructure.MemberReader
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

private val log = KotlinLogging.logger {}

class JwtAuthenticationFilter(
    private val memberReader: MemberReader,
    private val deviceProfileReader: DeviceProfileReader,
    private val tokenProvider: TokenProvider,
    private val blackListRepository: BlackListRepository,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val accessToken = resolveToken(request)
            val memberId = tokenProvider.extractMemberId(accessToken, TOKEN_TYPE_ACCESS)
            val deviceProfileId = tokenProvider.extractProfileId(accessToken)

            if (isBlackList(accessToken)) {
                throw CustomException(ErrorCode.TOKEN_INVALID)
            }

            val authentication = getAuthentication(memberId, deviceProfileId)

            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            log.warn(e) { "[JwtAuthenticationFilter.doFilterInternal] fail to parse token" }
            request.setAttribute(ATTRIBUTE_EXCEPTION_KEY, e)
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

    private fun getAuthentication(
        memberId: Long,
        deviceProfileId: Long,
    ): Authentication {
        val member =
            memberReader.findById(memberId)
                ?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)

        val deviceProfile = deviceProfileReader.getById(deviceProfileId)

        return UsernamePasswordAuthenticationToken(
            member.id,
            deviceProfile.id,
            listOf(
                SimpleGrantedAuthority(member.role.type),
                SimpleGrantedAuthority(member.state.name),
            ),
        )
    }
}
