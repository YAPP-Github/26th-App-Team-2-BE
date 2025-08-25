package com.yapp.brake.statistic.model

import java.time.LocalDate

data class DailySessionStatistic(
    val deviceProfileId: Long,
    val date: LocalDate,
    val actualMinutes: Long = 0L,
    val goalMinutes: Long = 0L,
) {
    fun add(deviceUsage: DeviceUsage): DailySessionStatistic {
        if (deviceUsage.start.toLocalDate() != date) {
            return this
        }

        return copy(
            actualMinutes = actualMinutes + deviceUsage.toActualMinutes(),
            goalMinutes = goalMinutes + deviceUsage.goalMinutes,
        )
    }
}
