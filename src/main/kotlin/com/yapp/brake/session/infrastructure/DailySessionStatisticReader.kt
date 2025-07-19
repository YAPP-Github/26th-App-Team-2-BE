package com.yapp.brake.session.infrastructure

import com.yapp.brake.session.model.DailySessionStatistic
import com.yapp.brake.session.model.SessionStatistics
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
