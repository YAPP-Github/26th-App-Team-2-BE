package com.yapp.brake.common.security.config

import com.yapp.brake.auth.infrastructure.redis.RedisBlackListRepository
import com.yapp.brake.auth.service.JwtTokenProvider
import com.yapp.brake.common.filter.JwtAuthenticationFilter
import com.yapp.brake.common.security.exception.ForbiddenHandler
import com.yapp.brake.common.security.exception.UnauthenticatedEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val unauthenticatedEntryPoint: UnauthenticatedEntryPoint,
    private val forbiddenHandler: ForbiddenHandler,
    private val requestLoggingFilter: CommonsRequestLoggingFilter,
    private val jwtTokenProvider: JwtTokenProvider,
    private val blackListRepository: RedisBlackListRepository,
    private val corsConfigurationSource: CorsConfigurationSource,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .cors { it.configurationSource(corsConfigurationSource) }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .rememberMe { it.disable() }
            .requestCache { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(*allowedUrls.toTypedArray())
                    .permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(unauthenticatedEntryPoint)
                    .accessDeniedHandler(forbiddenHandler)
            }
            .addFilterBefore(requestLoggingFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider, blackListRepository),
                UsernamePasswordAuthenticationFilter::class.java,
            )

        return http.build()
    }

    companion object {
        private val allowedUrls = listOf("/health", "/v1/auth/login", "/static/**", "/v1/swagger")
    }
}
