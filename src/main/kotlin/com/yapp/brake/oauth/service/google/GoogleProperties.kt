package com.yapp.brake.oauth.service.google

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "oauth.google")
data class GoogleProperties(
    var clientId: String = "",
    var clientSecret: String = "",
    var redirectUri: String = "",
)
