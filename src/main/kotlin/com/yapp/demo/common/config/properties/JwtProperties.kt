package com.yapp.demo.common.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    val accessTokenExpiryTime: Long,
    val refreshTokenExpiryTime: Long,
)
