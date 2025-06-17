package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.external.OAuthProvider
import com.yapp.demo.auth.external.OAuthUserInfo
import com.yapp.demo.common.enums.SocialProvider
import org.springframework.stereotype.Component

@Component
class AppleAuthProvider : OAuthProvider {
    override fun getAccessToken(code: String): String {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(token: String): OAuthUserInfo {
        TODO("Not yet implemented")
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.APPLE
}
