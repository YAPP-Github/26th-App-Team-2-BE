package com.yapp.brake.auth.controller

import andDocument
import com.yapp.brake.auth.dto.request.LogoutRequest
import com.yapp.brake.auth.dto.request.OAuthLoginRequest
import com.yapp.brake.auth.dto.request.OAuthWithdrawRequest
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
import com.yapp.brake.support.restdocs.toJsonString
import com.yapp.brake.support.restdocs.type
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.doNothing
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import requestBody
import responseBody
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

        `when`(authUseCase.login(request))
            .thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.post("/v1/auth/login")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument(
                "auth-login",
                Tag.AUTH,
                requestBody(
                    "provider" type STRING means "소셜 로그인 타입",
                    "authorizationCode" type STRING means "인가 코드",
                    "deviceId" type STRING means "모바일 디바이스 식별자",
                ),
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.accessToken" type STRING means "액세스 토큰",
                    "data.refreshToken" type STRING means "리프레시 토큰",
                    "data.memberState" type STRING means "유저의 상태 ",
                    "code" type NUMBER means "HTTP 코드",
                ),
            )
    }

    @Test
    fun `토큰 재발급 API`() {
        val request = RefreshTokenRequest("refreshToken")
        val response = ApiResponse.success(RefreshTokenResponse("access-token", "refresh-token"))

        `when`(authUseCase.refreshToken(request.refreshToken))
            .thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.post("/v1/auth/refresh")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument(
                "auth-refresh",
                Tag.AUTH,
                requestBody(
                    "refreshToken" type STRING means "기존 리프레시 토큰",
                ),
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.accessToken" type STRING means "액세스 토큰",
                    "data.refreshToken" type STRING means "리프레시 토큰",
                    "code" type NUMBER means "HTTP 코드",
                ),
            )
    }

    @Test
    fun `로그아웃 API`() {
        val request = LogoutRequest("accessToken")

        doNothing().`when`(authUseCase).logout(request.accessToken)

        val builder =
            RestDocumentationRequestBuilders.post("/v1/auth/logout")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isNoContent)
            .andDocument(
                "auth-logout",
                Tag.AUTH,
                requestBody(
                    "accessToken" type STRING means "기존 액세스 토큰",
                ),
            )
    }

    @Test
    fun `탈퇴 API`() {
        val request = OAuthWithdrawRequest(SocialProvider.KAKAO, "credential")

        doNothing().`when`(authUseCase).withdraw(request.provider, request.authorizationCode!!)

        val builder =
            RestDocumentationRequestBuilders.delete("/v1/auth/withdraw")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isNoContent)
            .andDocument(
                "auth-revoke",
                Tag.AUTH,
                requestBody(
                    "provider" type STRING means "소셜 로그인 타입",
                    "authorizationCode" type STRING means "인가 정보",
                ),
            )
    }
}
