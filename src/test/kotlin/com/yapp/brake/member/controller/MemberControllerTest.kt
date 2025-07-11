package com.yapp.brake.member.controller

import andDocument
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.member.dto.request.UpdateNicknameRequest
import com.yapp.brake.member.dto.response.MemberResponse
import com.yapp.brake.member.model.MemberState
import com.yapp.brake.support.RestApiTestBase
import com.yapp.brake.support.restdocs.NUMBER
import com.yapp.brake.support.restdocs.OBJECT
import com.yapp.brake.support.restdocs.STRING
import com.yapp.brake.support.restdocs.Tag
import com.yapp.brake.support.restdocs.toJsonString
import com.yapp.brake.support.restdocs.type
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import requestBody
import responseBody

class MemberControllerTest : RestApiTestBase() {
    @Test
    fun `내정보 조회 API`() {
        val memberId = 1L
        val response =
            ApiResponse.success(
                MemberResponse(
                    nickname = "brake-nickname",
                    state = MemberState.ACTIVE.name,
                ),
            )

        // SecurityContext 설정
        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), null)
        SecurityContextHolder.getContext().authentication = authentication

        whenever(memberUseCase.getMember(memberId)).thenReturn(response.data)

        val builder = RestDocumentationRequestBuilders.get("/v1/members/me")

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument(
                "members-me-get",
                Tag.MEMBER,
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.nickname" type STRING means "닉네임",
                    "data.state" type STRING means "상태",
                    "code" type NUMBER means "HTTP 코드",
                ),
            )

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `내정보 수정 API`() {
        val request =
            UpdateNicknameRequest(
                nickname = "modifiedNickname",
            )
        val response =
            ApiResponse.success(
                MemberResponse(
                    nickname = request.nickname,
                    state = MemberState.ACTIVE.name,
                ),
            )

        whenever(memberUseCase.update(request.nickname)).thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.patch("/v1/members/me")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument(
                "members-me-update",
                Tag.MEMBER,
                requestBody(
                    "nickname" type STRING means "변경하려는 닉네임",
                ),
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.nickname" type STRING means "닉네임",
                    "data.state" type STRING means "상태",
                    "code" type NUMBER means "HTTP 코드",
                ),
            )
    }

    @Test
    fun `탈퇴 API`() {
        val memberId = 1L
        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), null)
        SecurityContextHolder.getContext().authentication = authentication

        doNothing().whenever(memberUseCase).delete(memberId)

        val builder =
            RestDocumentationRequestBuilders.delete("/v1/members/me")

        mockMvc.perform(builder)
            .andExpect(status().isNoContent)
            .andDocument(
                "member-delete",
                Tag.MEMBER,
            )

        SecurityContextHolder.clearContext()
    }
}
