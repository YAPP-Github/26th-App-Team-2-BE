package com.yapp.demo.oauth.service

import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.oauth.model.OAuthUserInfo

interface OAuthProvider {
    fun getAccessToken(code: String): String

    fun getUserInfo(token: String): OAuthUserInfo

    fun withdraw(credential: String)

    fun supports(socialType: SocialProvider): Boolean
}
