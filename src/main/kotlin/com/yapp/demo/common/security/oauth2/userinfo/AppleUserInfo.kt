package com.yapp.demo.common.security.oauth2.userinfo

import org.springframework.security.core.authority.SimpleGrantedAuthority

data class AppleUserInfo(
    private val attributes: Map<String, Any>,
) : OAuth2UserInfo {
    private val attrs = attributes
    override val id: String = attrs["response"] as String
    override val email: String = attrs["email"] as String

    override fun getName(): String = (attrs["name"] as? String) ?: ""

    override fun getAttributes(): Map<String, Any> = attributes

    override fun getAuthorities(): List<SimpleGrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))
}
