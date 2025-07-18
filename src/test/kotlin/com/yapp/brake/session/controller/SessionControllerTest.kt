package com.yapp.brake.session.controller

import andDocument
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.dto.response.DailySessionStatisticsResponse
import com.yapp.brake.session.dto.response.SessionStatisticsResponse
import com.yapp.brake.support.RestApiTestBase
import com.yapp.brake.support.fixture.model.sessionFixture
import com.yapp.brake.support.restdocs.ARRAY
import com.yapp.brake.support.restdocs.DATETIME
import com.yapp.brake.support.restdocs.NUMBER
import com.yapp.brake.support.restdocs.OBJECT
import com.yapp.brake.support.restdocs.STRING
import com.yapp.brake.support.restdocs.Tag
import com.yapp.brake.support.restdocs.means
import com.yapp.brake.support.restdocs.queryParameters
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
        val session = sessionFixture()
        val request =
            AddSessionRequest(
                groupId = session.groupId,
                start = session.start,
                end = session.end,
                goalTime = session.goalTime,
                snoozeUnit = session.snooze.unit,
                snoozeCount = session.snooze.count,
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
                request,
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
                    "start" type DATETIME means "세션 시작 시간",
                    "end" type DATETIME means "세션 종료 시간",
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

    @Test
    fun `앱 사용 통계 조회 API`() {
        val memberId = 1L
        val start = LocalDate.of(2025, 7, 17)
        val end = LocalDate.of(2025, 7, 18)

        val response =
            ApiResponse.success(
                HttpStatus.OK.value(),
                SessionStatisticsResponse(
                    listOf(
                        DailySessionStatisticsResponse(
                            date = start,
                            dayOfWeek = start.dayOfWeek,
                            actualTime = LocalTime.of(1, 20),
                            goalTime = LocalTime.of(1, 30),
                        ),
                        DailySessionStatisticsResponse(
                            date = end,
                            dayOfWeek = end.dayOfWeek,
                            actualTime = LocalTime.of(2, 20),
                            goalTime = LocalTime.of(2, 0),
                        ),
                    ),
                ),
            )

        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), null)
        SecurityContextHolder.getContext().authentication = authentication

        whenever(
            sessionUseCase.get(
                memberId,
                startDate = start,
                endDate = end,
            ),
        ).thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.get("/v1/session")
                .queryParam("start", start.toString())
                .queryParam("end", end.toString())

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument(
                "session-get",
                Tag.SESSION,
                queryParameters(
                    "start" means "통계 조회 시작일",
                    "end" means "통계 조회 종료일",
                ),
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.statistics" type ARRAY means "통계 목록",
                    "data.statistics[].date" type STRING means "날짜",
                    "data.statistics[].dayOfWeek" type STRING means "요일",
                    "data.statistics[].actualTime" type STRING means "실제 사용 시간",
                    "data.statistics[].goalTime" type STRING means "목표 사용 시간",
                    "code" type NUMBER means "HTTP 코드",
                ),
            )
    }
}
