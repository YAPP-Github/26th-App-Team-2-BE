package com.yapp.demo.auth.external

import com.yapp.demo.auth.external.userinfo.OAuthUserInfo
import com.yapp.demo.common.enums.SocialProvider

interface OAuthProvider<T : OAuthUserInfo> {
    fun getAccessToken(code: String): String

    fun getUserInfo(token: String): T

    fun supports(socialType: SocialProvider): Boolean
}
