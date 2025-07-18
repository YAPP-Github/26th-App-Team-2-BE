package com.yapp.brake.session.model

import java.time.LocalDate

data class DailySessionStatistics(
    val memberId: Long,
    val date: LocalDate,
    val actualTime: Long = 0L,
    val goalTime: Long = 0L,
) {
    fun update(session: Session): DailySessionStatistics {
        return copy(
            actualTime = actualTime + session.actualTime(),
            goalTime = goalTime + session.goalTime,
        )
    }
}
