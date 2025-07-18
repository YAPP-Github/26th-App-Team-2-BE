package com.yapp.brake.session.dto.response

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class DailySessionStatisticsResponse(
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
    val actualTime: LocalTime,
    val goalTime: LocalTime,
) {
    companion object {
        fun create(
            date: LocalDate,
            actualTime: Long,
            goalTime: Long,
        ) = DailySessionStatisticsResponse(
            date = date,
            dayOfWeek = date.dayOfWeek,
            actualTime = LocalTime.ofSecondOfDay(actualTime),
            goalTime = LocalTime.ofSecondOfDay(goalTime),
        )
    }
}
