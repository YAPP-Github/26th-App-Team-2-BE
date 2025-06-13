package com.yapp.demo.common.security.oauth2.userinfo

enum class SocialProvider(
    private val id: String,
    private val creator: (Map<String, Any>) -> OAuth2UserInfo,
) {
    KAKAO("kakao", ::KakaoUserInfo),
    APPLE("apple", ::AppleUserInfo),
    ;

    fun createUserInfo(attributes: Map<String, Any>): OAuth2UserInfo = creator(attributes)

    companion object {
        fun from(registrationId: String) =
            entries.find { it.id == registrationId }
                ?: throw IllegalArgumentException("Unknown provider: $registrationId")
    }
}
