package com.yapp.demo.common.security.oauth2.userinfo

interface OAuth2UserInfo {
    val id: String
    val email: String
    val name: String
}
