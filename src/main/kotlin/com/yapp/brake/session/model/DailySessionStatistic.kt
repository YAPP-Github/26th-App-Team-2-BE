package com.yapp.brake.session.model

import java.time.LocalDate

data class DailySessionStatistic(
    val memberId: Long,
    val date: LocalDate,
    val actualMinutes: Long = 0L,
    val goalMinutes: Long = 0L,
) {
    fun add(session: Session): DailySessionStatistic {
        if (session.start.toLocalDate() != date) {
            return this
        }

        return copy(
            actualMinutes = actualMinutes + session.toActualMinutes(),
            goalMinutes = goalMinutes + session.goalMinutes,
        )
    }
}
