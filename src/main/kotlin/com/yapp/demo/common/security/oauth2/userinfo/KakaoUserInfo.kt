package com.yapp.demo.common.security.oauth2.userinfo

import org.springframework.security.core.authority.SimpleGrantedAuthority

class KakaoUserInfo(
    private val attributes: Map<String, Any>,
) : OAuth2UserInfo {
    private val account: Map<String, Any> =
        (attributes["kakao_account"] as? Map<String, Any>)
            ?: throw IllegalArgumentException("Kakao account data is missing")

    override val id: String = account["response"] as String
    override val email: String = account["email"] as String

    override fun getName(): String {
        val profile = account["profile"] as Map<String, Any>
        return profile["nickname"] as String
    }

    override fun getAttributes(): Map<String, Any> = attributes

    override fun getAuthorities(): List<SimpleGrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))
}
