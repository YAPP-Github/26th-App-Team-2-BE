package com.yapp.brake.support.fixture.model

import com.yapp.brake.statistic.model.DailySessionStatistic
import java.time.LocalDate

fun dailySessionStatisticsFixture(
    memberId: Long = 1L,
    date: LocalDate = LocalDate.of(2025, 7, 18),
    actualMinutes: Long = 30L,
    goalMinutes: Long = 30L,
) = DailySessionStatistic(
    memberId,
    date,
    actualMinutes,
    goalMinutes,
)
