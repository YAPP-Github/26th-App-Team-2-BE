package com.yapp.brake.security.config

import com.yapp.brake.auth.infrastructure.BlackListRepository
import com.yapp.brake.auth.infrastructure.TokenProvider
import com.yapp.brake.constants.ALLOWED_URIS
import com.yapp.brake.deviceprofile.infrastructure.DeviceProfileReader
import com.yapp.brake.filter.JwtAuthenticationFilter
import com.yapp.brake.filter.MemberStateFilter
import com.yapp.brake.member.infrastructure.MemberReader
import com.yapp.brake.security.exception.ForbiddenHandler
import com.yapp.brake.security.exception.UnauthenticatedEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.ExceptionTranslationFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val unauthenticatedEntryPoint: UnauthenticatedEntryPoint,
    private val forbiddenHandler: ForbiddenHandler,
    private val requestLoggingFilter: CommonsRequestLoggingFilter,
    private val corsConfigurationSource: CorsConfigurationSource,
) {
    @Bean
    @Order(1)
    fun publicSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.securityMatcher(*ALLOWED_URIS.toTypedArray())
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .cors { it.configurationSource(corsConfigurationSource) }
            .securityContext { it.disable() }
            .build()
    }

    @Bean
    @Order(2)
    fun holdMemberFilterChain(
        memberReader: MemberReader,
        deviceProfileReader: DeviceProfileReader,
        tokenProvider: TokenProvider,
        blackListRepository: BlackListRepository,
        http: HttpSecurity,
    ): SecurityFilterChain {
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
            .securityMatcher { request ->
                request.requestURI == "/v1/members/me" &&
                    (request.method == HttpMethod.PATCH.name() || request.method == HttpMethod.DELETE.name())
            }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .exceptionHandling { it.authenticationEntryPoint(unauthenticatedEntryPoint) }
            .addFilterBefore(requestLoggingFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(
                JwtAuthenticationFilter(memberReader, deviceProfileReader, tokenProvider, blackListRepository),
                UsernamePasswordAuthenticationFilter::class.java,
            )

        return http.build()
    }

    @Bean
    @Order(3)
    fun activeMemberFilterChain(
        memberReader: MemberReader,
        deviceProfileReader: DeviceProfileReader,
        tokenProvider: TokenProvider,
        blackListRepository: BlackListRepository,
        http: HttpSecurity,
    ): SecurityFilterChain {
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
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .exceptionHandling {
                it.authenticationEntryPoint(unauthenticatedEntryPoint)
                    .accessDeniedHandler(forbiddenHandler)
            }
            .addFilterBefore(requestLoggingFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(
                JwtAuthenticationFilter(memberReader, deviceProfileReader, tokenProvider, blackListRepository),
                UsernamePasswordAuthenticationFilter::class.java,
            )
            .addFilterAfter(MemberStateFilter(), ExceptionTranslationFilter::class.java)

        return http.build()
    }
}
