package com.yapp.brake.statistic.service

import com.yapp.brake.statistic.dto.response.SessionStatisticsResponse
import java.time.LocalDate

interface StatisticUseCase {
    fun get(
        deviceProfileId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): SessionStatisticsResponse
}
