package com.yapp.demo.common.security.config

import com.yapp.demo.common.security.exception.ForbiddenHandler
import com.yapp.demo.common.security.exception.UnauthenticatedEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val unauthenticatedEntryPoint: UnauthenticatedEntryPoint,
    private val forbiddenHandler: ForbiddenHandler,
    private val customOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
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
                it
                    .requestMatchers(
                        "/oauth2/**",
                        "/login/oauth2/**",
                        "/health",
                    )
                    .permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it
                    .authenticationEntryPoint(unauthenticatedEntryPoint)
                    .accessDeniedHandler(forbiddenHandler)
            }
            .oauth2Login {
                it
                    .userInfoEndpoint { endpoint -> endpoint.userService(customOAuth2UserService) }
                    .successHandler { _, _, _ -> run { println("success") } }
                    .failureHandler { _, _, _ -> run { println("fail") } }
            }

        return http.build()
    }
}
