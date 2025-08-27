package com.yapp.brake.statistic.dto.response

import com.yapp.brake.statistic.model.DailySessionStatistic
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class DailySessionStatisticResponse(
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
    val actualTime: LocalTime,
    val goalTime: LocalTime,
) {
    companion object {
        fun from(dailySessionStatistic: DailySessionStatistic) =
            DailySessionStatisticResponse(
                date = dailySessionStatistic.date,
                dayOfWeek = dailySessionStatistic.date.dayOfWeek,
                actualTime = LocalTime.MIN.plusMinutes(dailySessionStatistic.actualMinutes),
                goalTime = LocalTime.MIN.plusMinutes(dailySessionStatistic.goalMinutes),
            )
    }
}
