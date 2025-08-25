package com.yapp.brake.oauth.apple

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "oauth.apple")
data class AppleProperties(
    var clientId: String = "",
    var teamId: String = "",
    var keyId: String = "",
    var audience: String = "",
    var authKeyPath: String = "",
    var headerField: String = "kid",
)
