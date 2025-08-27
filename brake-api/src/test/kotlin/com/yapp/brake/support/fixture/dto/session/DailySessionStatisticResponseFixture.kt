package com.yapp.brake.support.fixture.dto.session

import com.yapp.brake.statistic.dto.response.DailySessionStatisticResponse
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

fun dailySessionStatisticResponseFixture(
    date: LocalDate = LocalDate.of(2025, 1, 1),
    dayOfWeek: DayOfWeek = date.dayOfWeek,
    actualTime: LocalTime = LocalTime.of(0, 0),
    goalTime: LocalTime = LocalTime.of(0, 0),
): DailySessionStatisticResponse {
    return DailySessionStatisticResponse(
        date = date,
        dayOfWeek = dayOfWeek,
        actualTime = actualTime,
        goalTime = goalTime,
    )
}
