package com.yapp.demo.common.security.oauth2.userinfo

class KakaoUserInfo(
    attributes: Map<String, Any>,
) : OAuth2UserInfo {
    private val account: Map<String, Any> =
        (attributes["kakao_account"] as? Map<String, Any>)
            ?: throw IllegalArgumentException("Kakao account data is missing")

    override val id: String
        get() {
            return account["response"] as String
        }

    override val name: String
        get() {
            val profile = account["profile"] as Map<String, Any>
            return profile["nickname"] as String
        }
    override val email: String
        get() {
            return account["email"] as String
        }
}
