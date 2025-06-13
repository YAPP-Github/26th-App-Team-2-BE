package com.yapp.demo.common.security.oauth2.userinfo

class AppleUserInfo(
    attributes: Map<String, Any>,
) : OAuth2UserInfo {
    private val attrs = attributes
    override val id: String
        get() {
            return attrs["response"] as String
        }

    override val name: String
        get() {
            return attrs["name"] as String
        }

    override val email: String
        get() {
            return attrs["email"] as String
        }
}
