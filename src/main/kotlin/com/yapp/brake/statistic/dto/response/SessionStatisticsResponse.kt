package com.yapp.brake.statistic.dto.response

import com.yapp.brake.statistic.model.SessionStatistics

data class SessionStatisticsResponse(
    val statistics: List<DailySessionStatisticResponse> = emptyList(),
) {
    companion object {
        fun from(sessionStatistics: SessionStatistics): SessionStatisticsResponse {
            val statistics = sessionStatistics.statistics.map { DailySessionStatisticResponse.from(it) }
            return SessionStatisticsResponse(statistics)
        }
    }
}
