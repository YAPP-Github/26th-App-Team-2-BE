package com.yapp.demo.auth.external

import com.yapp.demo.auth.external.userinfo.AppleUserInfo
import com.yapp.demo.common.enums.SocialProvider
import org.springframework.stereotype.Component

@Component
class AppleAuthProvider : OAuthProvider<AppleUserInfo> {
    override fun getAccessToken(code: String): String {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(token: String): AppleUserInfo {
        TODO("Not yet implemented")
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.APPLE
}
