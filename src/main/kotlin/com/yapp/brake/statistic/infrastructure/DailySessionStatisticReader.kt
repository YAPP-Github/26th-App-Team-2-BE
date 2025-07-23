package com.yapp.brake.statistic.infrastructure

import com.yapp.brake.statistic.model.DailySessionStatistic
import com.yapp.brake.statistic.model.SessionStatistics
import java.time.LocalDate

interface DailySessionStatisticReader {
    fun getById(
        memberId: Long,
        date: LocalDate,
    ): DailySessionStatistic

    fun getAllByIds(
        memberId: Long,
        dates: List<LocalDate>,
    ): SessionStatistics
}
