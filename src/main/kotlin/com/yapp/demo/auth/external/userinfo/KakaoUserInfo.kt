package com.yapp.demo.auth.external.userinfo

data class KakaoUserInfo(
    override val id: String,
    override val email: String,
) : OAuthUserInfo
