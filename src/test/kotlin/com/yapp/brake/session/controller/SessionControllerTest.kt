package com.yapp.brake.session.controller

import andDocument
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.support.RestApiTestBase
import com.yapp.brake.support.fixture.model.sessionFixture
import com.yapp.brake.support.restdocs.DATETIME
import com.yapp.brake.support.restdocs.NUMBER
import com.yapp.brake.support.restdocs.OBJECT
import com.yapp.brake.support.restdocs.Tag
import com.yapp.brake.support.restdocs.toJsonString
import com.yapp.brake.support.restdocs.type
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class SessionControllerTest : RestApiTestBase() {
    @Test
    fun `세션 추가 API`() {
        val memberId = 1L
        val deviceProfileId = 1L
        val session = sessionFixture()
        val request =
            AddSessionRequest(
                groupId = session.groupId,
                start = session.start,
                end = session.end,
                goalMinutes = session.goalMinutes,
                snoozeUnit = session.snooze.unit,
                snoozeCount = session.snooze.count,
            )

        val response =
            ApiResponse.success(
                status = HttpStatus.CREATED.value(),
                data = AddSessionResponse(sessionId = 1L),
            )

        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), deviceProfileId)
        SecurityContextHolder.getContext().authentication = authentication

        whenever(
            sessionUseCase.add(
                any(),
                any(),
            ),
        ).thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.post("/v1/session")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument("session-add") {
                tag(Tag.SESSION)
                requestSchema(request::class.java.simpleName)
                responseSchema(response.data!!::class.java.simpleName)
                requestBody(
                    "groupId" type NUMBER means "관리 앱 그룹",
                    "start" type DATETIME means "세션 시작 시간",
                    "end" type DATETIME means "세션 종료 시간",
                    "goalMinutes" type NUMBER means "세션 목표 시간(분 단위)",
                    "snoozeUnit" type NUMBER means "스누즈 단위(분 단위)",
                    "snoozeCount" type NUMBER means "스누즈 횟수",
                )
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.sessionId" type NUMBER means "세션 식별자",
                    "status" type NUMBER means "HTTP 코드",
                )
            }

        SecurityContextHolder.clearContext()
    }
}
