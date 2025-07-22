package com.yapp.brake.statistic.model

import java.time.LocalDate

data class DailySessionStatistic(
    val memberId: Long,
    val date: LocalDate,
    val actualMinutes: Long = 0L,
    val goalMinutes: Long = 0L,
) {
    fun add(memberUsage: MemberUsage): DailySessionStatistic {
        if (memberUsage.start.toLocalDate() != date) {
            return this
        }

        return copy(
            actualMinutes = actualMinutes + memberUsage.toActualMinutes(),
            goalMinutes = goalMinutes + memberUsage.goalMinutes,
        )
    }
}
