package com.yapp.demo.oauth.service.apple

import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.oauth.model.OAuthUserInfo
import com.yapp.demo.oauth.service.OAuthProvider
import org.springframework.stereotype.Component

@Component
class AppleAuthProvider(
    private val appleClientSecretGenerator: AppleClientSecretGenerator,
    private val appleTokenProvider: AppleTokenProvider,
) : OAuthProvider {
    override fun getAccessToken(code: String): String {
        val clientSecret = appleClientSecretGenerator.getClientSecret()
        return appleTokenProvider.getToken(code, clientSecret).idToken
    }

    override fun getUserInfo(token: String): OAuthUserInfo {
        val claims = appleTokenProvider.verifyAndParse(token)

        return OAuthUserInfo(
            id = claims.subject,
            email = claims["email"] as? String ?: "",
        )
    }

    override fun withdraw(code: String) {
        val clientSecret = appleClientSecretGenerator.getClientSecret()
        val accessToken = appleTokenProvider.getToken(code, clientSecret).accessToken

        appleTokenProvider.revokeToken(accessToken, clientSecret)
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.APPLE
}
