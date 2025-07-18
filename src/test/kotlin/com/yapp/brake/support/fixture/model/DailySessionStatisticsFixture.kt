package com.yapp.brake.support.fixture.model

import com.yapp.brake.session.model.DailySessionStatistics
import java.time.LocalDate

fun dailySessionStatisticsFixture(
    memberId: Long = 1L,
    date: LocalDate = LocalDate.of(2025, 7, 18),
    actualTime: Long = 1800L,
    goalTime: Long = 1800L,
) = DailySessionStatistics(
    memberId,
    date,
    actualTime,
    goalTime,
)
