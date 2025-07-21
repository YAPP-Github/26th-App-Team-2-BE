package com.yapp.brake.session.model

import java.time.Duration
import java.time.LocalDateTime

data class Session(
    val id: Long = 0L,
    val groupId: Long,
    val memberId: Long,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val goalMinutes: Long,
    val snooze: Snooze,
) {
    companion object {
        fun create(
            memberId: Long,
            groupId: Long,
            start: LocalDateTime,
            end: LocalDateTime,
            goalMinutes: Long,
            snoozeUnit: Int,
            snoozeCount: Int,
        ) = Session(
            memberId = memberId,
            groupId = groupId,
            start = start,
            end = end,
            goalMinutes = goalMinutes,
            snooze =
                Snooze(
                    unit = snoozeUnit,
                    count = snoozeCount,
                ),
        )
    }

    fun toActualMinutes(): Long {
        return Duration.between(start, end).toMinutes()
    }

    fun splitByDate(): List<Session> {
        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()

        if (startDate == endDate) {
            return listOf(this)
        }

        val midnight = start.toLocalDate().plusDays(1).atStartOfDay()
        val goalBeforeMidnight = Duration.between(start, midnight).toMinutes()
        val goalAfterMidnight = goalMinutes - goalBeforeMidnight
        return listOf(
            copy(
                start = start,
                end = midnight,
                goalMinutes = goalBeforeMidnight,
            ),
            copy(
                start = midnight,
                end = end,
                goalMinutes = goalAfterMidnight,
            ),
        )
    }
}
