package com.yapp.brake.oauth.service.kakao

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "oauth.kakao")
data class KakaoProperties(
    var clientId: String = "",
    var redirectUri: String = "",
    var adminKey: String = "",
)
