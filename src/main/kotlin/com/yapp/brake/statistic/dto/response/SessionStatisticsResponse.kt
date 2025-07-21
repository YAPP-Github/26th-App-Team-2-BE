package com.yapp.brake.statistic.dto.response

data class SessionStatisticsResponse(
    val statistics: List<DailySessionStatisticResponse> = emptyList(),
)
