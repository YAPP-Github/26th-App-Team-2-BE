package com.yapp.demo.auth.external

import com.yapp.demo.auth.external.userinfo.KakaoUserInfo
import com.yapp.demo.common.enums.SocialProvider
import org.springframework.stereotype.Component

@Component
class KakaoAuthProvider : OAuthProvider<KakaoUserInfo> {
    override fun getAccessToken(code: String): String {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(token: String): KakaoUserInfo {
        TODO("Not yet implemented")
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.KAKAO
}
