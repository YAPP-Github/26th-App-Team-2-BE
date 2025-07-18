package com.yapp.brake.session.dto.response

data class SessionStatisticsResponse(
    val statistics: List<DailySessionStatisticsResponse> = emptyList(),
)
