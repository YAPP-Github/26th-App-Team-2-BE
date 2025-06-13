package com.yapp.demo.common.security.oauth2.service

import com.yapp.demo.common.security.oauth2.userinfo.SocialProvider
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val defaultOAuth2UserService: DefaultOAuth2UserService = DefaultOAuth2UserService(),
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    override fun loadUser(request: OAuth2UserRequest): OAuth2User {
        val oauthUser = defaultOAuth2UserService.loadUser(request)

        SocialProvider.from(request.clientRegistration.registrationId)
            .createUserInfo(oauthUser.attributes)

        // TODO(user save || get)
        return oauthUser
    }
}
