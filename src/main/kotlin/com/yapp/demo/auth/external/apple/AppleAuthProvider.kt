package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.external.OAuthProvider
import com.yapp.demo.auth.external.OAuthUserInfo
import com.yapp.demo.auth.internal.AppleClientSecretGenerator
import com.yapp.demo.common.enums.SocialProvider
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
            email = claims["email"] as? String ?: "empty",
        )
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.APPLE
}
