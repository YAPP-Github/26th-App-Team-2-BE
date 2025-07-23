package com.yapp.brake.statistic.controller

import andDocument
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.statistic.dto.response.DailySessionStatisticResponse
import com.yapp.brake.statistic.dto.response.SessionStatisticsResponse
import com.yapp.brake.support.RestApiTestBase
import com.yapp.brake.support.restdocs.ARRAY
import com.yapp.brake.support.restdocs.NUMBER
import com.yapp.brake.support.restdocs.OBJECT
import com.yapp.brake.support.restdocs.STRING
import com.yapp.brake.support.restdocs.Tag
import com.yapp.brake.support.restdocs.means
import com.yapp.brake.support.restdocs.type
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalTime

class StatisticControllerTest : RestApiTestBase() {
    val memberId = 1L
    val start = LocalDate.of(2025, 7, 17)
    val end = LocalDate.of(2025, 7, 18)
    val response =
        ApiResponse.success(
            HttpStatus.OK.value(),
            SessionStatisticsResponse(
                listOf(
                    DailySessionStatisticResponse(
                        date = start,
                        dayOfWeek = start.dayOfWeek,
                        actualTime = LocalTime.of(1, 20),
                        goalTime = LocalTime.of(1, 30),
                    ),
                    DailySessionStatisticResponse(
                        date = end,
                        dayOfWeek = end.dayOfWeek,
                        actualTime = LocalTime.of(2, 20),
                        goalTime = LocalTime.of(2, 0),
                    ),
                ),
            ),
        )

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `앱 사용 통계 조회 API`() {
        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), null)
        SecurityContextHolder.getContext().authentication = authentication

        whenever(statisticUseCase.get(memberId, start, end)).thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.get("/v1/statistics")
                .queryParam("start", start.toString())
                .queryParam("end", end.toString())

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument("statistic-get") {
                tag(Tag.STATISTIC)
                queryParameters(
                    "start" means "통계 조회 시작일" optional true,
                    "end" means "통계 조회 종료일" optional true,
                )
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.statistics" type ARRAY means "통계 목록",
                    "data.statistics[].date" type STRING means "날짜",
                    "data.statistics[].dayOfWeek" type STRING means "요일",
                    "data.statistics[].actualTime" type STRING means "실제 사용 시간",
                    "data.statistics[].goalTime" type STRING means "목표 사용 시간",
                    "code" type NUMBER means "HTTP 코드",
                )
            }
    }

    @Test
    fun `앱 사용 통계 조회 API - 파라미터 X`() {
        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), null)
        SecurityContextHolder.getContext().authentication = authentication

        whenever(
            statisticUseCase.get(
                any(),
                any(),
                any(),
            ),
        ).thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.get("/v1/statistics")

        mockMvc.perform(builder)
            .andExpect(status().isOk)
    }

    @ParameterizedTest
    @ValueSource(strings = ["start", "end"])
    fun `앱 사용 통계 조회 API - 파라미터 일부`(date: String) {
        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), null)
        SecurityContextHolder.getContext().authentication = authentication

        whenever(
            statisticUseCase.get(
                any(),
                any(),
                any(),
            ),
        ).thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.get("/v1/statistics")
                .queryParam(date, start.toString())

        mockMvc.perform(builder)
            .andExpect(status().isOk)
    }
}
