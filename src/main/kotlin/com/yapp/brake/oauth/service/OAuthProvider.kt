package com.yapp.brake.oauth.service

import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.oauth.model.OAuthUserInfo

interface OAuthProvider {
    fun getAccessToken(code: String): String

    fun getUserInfo(token: String): OAuthUserInfo

    fun withdraw(credential: String)

    fun supports(socialType: SocialProvider): Boolean
}
