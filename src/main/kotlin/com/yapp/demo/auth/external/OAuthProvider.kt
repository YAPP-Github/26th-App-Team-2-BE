package com.yapp.demo.auth.external

import com.yapp.demo.common.enums.SocialProvider

interface OAuthProvider {
    fun getAccessToken(code: String): String

    fun getUserInfo(token: String): OAuthUserInfo

    fun supports(socialType: SocialProvider): Boolean
}
