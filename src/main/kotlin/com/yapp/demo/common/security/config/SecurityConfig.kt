package com.yapp.demo.common.security.config

import com.yapp.demo.common.filter.JwtAuthenticationFilter
import com.yapp.demo.common.security.exception.ForbiddenHandler
import com.yapp.demo.common.security.exception.UnauthenticatedEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val unauthenticatedEntryPoint: UnauthenticatedEntryPoint,
    private val forbiddenHandler: ForbiddenHandler,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .rememberMe { it.disable() }
            .requestCache { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/health", "/v1/auth/login")
                    .permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(unauthenticatedEntryPoint)
                    .accessDeniedHandler(forbiddenHandler)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
