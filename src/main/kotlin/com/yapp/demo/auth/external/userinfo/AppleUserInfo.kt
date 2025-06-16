package com.yapp.demo.auth.external.userinfo

data class AppleUserInfo(
    override val id: String,
    override val email: String,
) : OAuthUserInfo
