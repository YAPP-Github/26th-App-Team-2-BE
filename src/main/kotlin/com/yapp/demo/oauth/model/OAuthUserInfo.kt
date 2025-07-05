package com.yapp.demo.oauth.model

import com.yapp.demo.common.enums.SocialProvider

data class OAuthUserInfo(
    val socialProvider: SocialProvider,
    val id: String,
    val email: String,
)
