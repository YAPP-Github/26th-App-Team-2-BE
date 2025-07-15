package com.yapp.brake.session.model

import java.time.LocalDateTime

data class Session(
    val id: Long = 0L,
    val groupId: Long,
    val memberId: Long,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val goalTime: Long,
    val snooze: Snooze,
) {
    companion object {
        fun create(
            memberId: Long,
            groupId: Long,
            start: LocalDateTime,
            end: LocalDateTime,
            goalTime: Long,
            snoozeUnit: Int,
            snoozeCount: Int,
        ) = Session(
            memberId = memberId,
            groupId = groupId,
            start = start,
            end = end,
            goalTime = goalTime,
            snooze =
                Snooze(
                    unit = snoozeUnit,
                    count = snoozeCount,
                ),
        )
    }
}
