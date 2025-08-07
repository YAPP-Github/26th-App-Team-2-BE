package com.yapp.brake.common.filter

import com.yapp.brake.member.model.MemberState
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.file.AccessDeniedException

private val log = KotlinLogging.logger {}

class MemberStateFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authorities =
            SecurityContextHolder.getContext()
                .authentication
                .authorities

        val memberState =
            authorities
                .map { it.authority }
                .firstOrNull { it in MemberState.entries.map { e -> e.name } }
                ?.let { MemberState.valueOf(it) }

        if (memberState != MemberState.ACTIVE) {
            log.error { "[MemberStateFilter.doFilterInternal] invalid memberState=$memberState" }
            throw AccessDeniedException("")
        }

        filterChain.doFilter(request, response)
    }
}
