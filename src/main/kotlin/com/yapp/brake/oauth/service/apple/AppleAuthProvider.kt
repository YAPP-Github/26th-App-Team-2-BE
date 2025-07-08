package com.yapp.brake.oauth.service.apple

import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.oauth.model.OAuthUserInfo
import com.yapp.brake.oauth.service.OAuthProvider
import org.springframework.stereotype.Component

@Component
class AppleAuthProvider(
    private val appleClientSecretGenerator: AppleClientSecretGenerator,
    private val appleTokenProvider: AppleTokenProvider,
) : OAuthProvider {
    override fun getOAuthUserInfo(code: String): OAuthUserInfo {
        val clientSecret = appleClientSecretGenerator.getClientSecret()
        val tokens = appleTokenProvider.getToken(code, clientSecret)
        val claims = appleTokenProvider.verifyAndParse(tokens.idToken)

        return OAuthUserInfo(
            socialProvider = SocialProvider.APPLE,
            credential = tokens.refreshToken,
            email = claims["email"] as? String ?: "",
        )
    }

    override fun withdraw(oAuthUserInfo: OAuthUserInfo) {
        val clientSecret = appleClientSecretGenerator.getClientSecret()
        appleTokenProvider.revokeToken(oAuthUserInfo.credential, clientSecret)
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.APPLE
}
