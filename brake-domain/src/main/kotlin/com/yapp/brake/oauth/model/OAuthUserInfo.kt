package com.yapp.brake.oauth.model

import com.yapp.brake.common.enums.SocialProvider

data class OAuthUserInfo(
    val socialProvider: SocialProvider,
    val credential: String,
    val email: String,
)
