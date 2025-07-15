package com.yapp.brake.session.controller

import andDocument
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.support.RestApiTestBase
import com.yapp.brake.support.restdocs.DATE
import com.yapp.brake.support.restdocs.NUMBER
import com.yapp.brake.support.restdocs.OBJECT
import com.yapp.brake.support.restdocs.TIME
import com.yapp.brake.support.restdocs.Tag
import com.yapp.brake.support.restdocs.toJsonString
import com.yapp.brake.support.restdocs.type
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import requestBody
import responseBody
import java.time.LocalDate
import java.time.LocalTime

class SessionControllerTest : RestApiTestBase() {
    @Test
    fun `세션 추가 API`() {
        val memberId = 1L
        val request =
            AddSessionRequest(
                groupId = 1L,
                date = LocalDate.now(),
                startTime = LocalTime.now().minusMinutes(30),
                endTime = LocalTime.now(),
                goalTime = 108000L,
                snoozeUnit = 5,
                snoozeCount = 0,
            )
        val response =
            ApiResponse.success(
                HttpStatus.CREATED.value(),
                AddSessionResponse(sessionId = 1L),
            )

        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), null)
        SecurityContextHolder.getContext().authentication = authentication

        whenever(
            sessionUseCase.add(
                memberId,
                request.groupId,
                request.date,
                request.startTime,
                request.endTime,
                request.goalTime,
                request.snoozeUnit,
                request.snoozeCount,
            ),
        ).thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.post("/v1/session")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument(
                "session-add",
                Tag.SESSION,
                requestBody(
                    "groupId" type NUMBER means "관리 앱 그룹",
                    "date" type DATE means "세션 날짜",
                    "startTime" type TIME means "세션 시작 시간",
                    "endTime" type TIME means "세션 종료 시간",
                    "goalTime" type NUMBER means "세션 목표 시간(초 단위)",
                    "snoozeUnit" type NUMBER means "스누즈 단위(분 단위)",
                    "snoozeCount" type NUMBER means "스누즈 횟수",
                ),
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.sessionId" type NUMBER means "세션 식별자",
                    "code" type NUMBER means "HTTP 코드",
                ),
            )
    }
}
