package com.yapp.brake.session.model

import com.yapp.brake.session.utils.generateBetweenDates
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
        val betweenDates = generateBetweenDates(start, end)

        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()

        return betweenDates.map { date ->
            val isFirst = date == startDate
            val isLast = date == endDate

            val segmentStart = if (isFirst) start else date.atStartOfDay()
            val segmentEnd = if (isLast) end else date.plusDays(1).atStartOfDay()

            copy(
                start = segmentStart,
                end = segmentEnd,
                goalMinutes = Duration.between(segmentStart, segmentEnd).toMinutes(),
            )
        }
    }
}
