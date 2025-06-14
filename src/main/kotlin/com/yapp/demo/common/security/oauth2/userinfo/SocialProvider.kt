package com.yapp.demo.common.security.oauth2.userinfo

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode.BAD_REQUEST

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
                ?: throw CustomException(BAD_REQUEST)
    }
}
