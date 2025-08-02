package com.yapp.brake.auth.controller

import andDocument
import com.yapp.brake.auth.dto.request.OAuthLoginRequest
import com.yapp.brake.auth.dto.request.RefreshTokenRequest
import com.yapp.brake.auth.dto.response.OAuthLoginResponse
import com.yapp.brake.auth.dto.response.RefreshTokenResponse
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.member.model.MemberState
import com.yapp.brake.support.RestApiTestBase
import com.yapp.brake.support.restdocs.NUMBER
import com.yapp.brake.support.restdocs.OBJECT
import com.yapp.brake.support.restdocs.STRING
import com.yapp.brake.support.restdocs.Tag
import com.yapp.brake.support.restdocs.description
import com.yapp.brake.support.restdocs.toJsonString
import com.yapp.brake.support.restdocs.type
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class AuthControllerTest : RestApiTestBase() {
    @Test
    fun `로그인 API`() {
        val request =
            OAuthLoginRequest(
                SocialProvider.KAKAO,
                "authorization-code",
                UUID.randomUUID().toString(),
            )

        val response =
            ApiResponse.success(
                OAuthLoginResponse(
                    "access-token",
                    "refresh-token",
                    MemberState.ACTIVE.name,
                ),
            )

        whenever(authUseCase.login(request))
            .thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.post("/v1/auth/login")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument("auth-login") {
                tag(Tag.AUTH)
                requestSchema(request::class.java.simpleName)
                responseSchema(response.data!!::class.java.simpleName)
                requestBody(
                    "provider" type STRING means "소셜 로그인 타입",
                    "authorizationCode" type STRING means "인가 코드",
                    "deviceName" type STRING means "모바일 디바이스 식별자",
                )
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.accessToken" type STRING means "액세스 토큰",
                    "data.refreshToken" type STRING means "리프레시 토큰",
                    "data.memberState" type STRING means "유저의 상태 ",
                    "status" type NUMBER means "HTTP 코드",
                )
            }
    }

    @Test
    fun `토큰 재발급 API`() {
        val request = RefreshTokenRequest("refreshToken")
        val response = ApiResponse.success(RefreshTokenResponse("access-token", "refresh-token"))

        whenever(authUseCase.refreshToken(request.refreshToken))
            .thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.post("/v1/auth/refresh")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument("auth-refresh") {
                tag(Tag.AUTH)
                requestSchema(request::class.java.simpleName)
                responseSchema(response.data!!::class.java.simpleName)
                requestBody(
                    "refreshToken" type STRING means "기존 리프레시 토큰",
                )
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.accessToken" type STRING means "액세스 토큰",
                    "data.refreshToken" type STRING means "리프레시 토큰",
                    "status" type NUMBER means "HTTP 코드",
                )
            }
    }

    @Test
    fun `로그아웃 API`() {
        val memberId = 1L
        val deviceProfileId = 1L
        val accessToken = "access-Token"
        val authentication = UsernamePasswordAuthenticationToken(memberId, deviceProfileId)
        SecurityContextHolder.getContext().authentication = authentication

        doNothing().whenever(authUseCase).logout(deviceProfileId, accessToken)

        val builder =
            RestDocumentationRequestBuilders.post("/v1/auth/logout")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isNoContent)
            .andDocument("auth-logout") {
                tag(Tag.AUTH)
                requestHeaders(HttpHeaders.AUTHORIZATION description "액세스 토큰")
            }
    }
}
