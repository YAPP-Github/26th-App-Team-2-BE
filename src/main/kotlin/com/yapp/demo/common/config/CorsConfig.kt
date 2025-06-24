package com.yapp.demo.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = ALLOWED_ORIGINS.toList()
        configuration.allowedMethods = ALLOWED_METHODS.toList()
        configuration.allowedHeaders = listOf("*")
        configuration.addExposedHeader(AUTHORIZATION_HEADER)
        configuration.maxAge = MAX_AGE

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    companion object {
        private val ALLOWED_ORIGINS = arrayOf("http://35.212.131.175/", "http://localhost:*")
        private val ALLOWED_METHODS = arrayOf("OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH")
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val MAX_AGE = 3600L
    }
}
