package com.yapp.brake.session.infrastructure

import com.yapp.brake.session.model.DailySessionStatistics
import java.time.LocalDate

interface DailySessionStatisticsReader {
    fun getById(
        memberId: Long,
        date: LocalDate,
    ): DailySessionStatistics

    fun getByIds(
        memberId: Long,
        dates: List<LocalDate>,
    ): List<DailySessionStatistics>
}
