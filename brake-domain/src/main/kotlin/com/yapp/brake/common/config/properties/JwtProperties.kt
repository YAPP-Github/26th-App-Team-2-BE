package com.yapp.brake.common.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var secret: String = "",
    var accessTokenExpiryTime: Long = 0L,
    var refreshTokenExpiryTime: Long = 0L,
)
