package com.yapp.brake.statistic.controller

import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.common.security.getMemberId
import com.yapp.brake.statistic.dto.request.QueryStatisticRequest
import com.yapp.brake.statistic.dto.response.SessionStatisticsResponse
import com.yapp.brake.statistic.service.StatisticUseCase
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/statistics")
class StatisticController(
    private val statisticUseCase: StatisticUseCase,
) {
    @GetMapping
    fun get(
        @Valid @ModelAttribute
        request: QueryStatisticRequest,
    ): ApiResponse<SessionStatisticsResponse> {
        val response = statisticUseCase.get(getMemberId(), request.start, request.end)
        return ApiResponse.success(response)
    }
}
