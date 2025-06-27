package com.yapp.demo.oauth.service.apple

import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.oauth.model.OAuthUserInfo
import com.yapp.demo.oauth.service.OAuthProvider
import org.springframework.stereotype.Component

@Component
class AppleAuthProvider(
    private val appleClientSecretGenerator: AppleClientSecretGenerator,
    private val appleIdTokenProvider: AppleIdTokenProvider,
) : OAuthProvider {
    override fun getAccessToken(code: String): String {
        val clientSecret = appleClientSecretGenerator.getClientSecret()
        return appleIdTokenProvider.getIdToken(code, clientSecret)
    }

    override fun getUserInfo(token: String): OAuthUserInfo {
        val claims = appleIdTokenProvider.verifyAndParse(token)

        return OAuthUserInfo(
            id = claims.subject,
            email = claims["email"] as? String ?: "",
        )
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.APPLE
}
