package com.yapp.brake.session.infrastructure

import com.yapp.brake.session.model.DailySessionStatistics
import com.yapp.brake.session.model.SessionStatistics
import java.time.LocalDate

interface DailySessionStatisticsReader {
    fun getById(
        memberId: Long,
        date: LocalDate,
    ): DailySessionStatistics

    fun getAllByIds(
        memberId: Long,
        dates: List<LocalDate>,
    ): SessionStatistics
}
