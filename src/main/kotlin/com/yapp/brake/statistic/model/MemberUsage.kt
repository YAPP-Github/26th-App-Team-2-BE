package com.yapp.brake.statistic.model

import java.time.Duration
import java.time.LocalDateTime

data class MemberUsage(
    val memberId: Long,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val goalMinutes: Long,
) {
    fun toActualMinutes(): Long {
        return Duration.between(start, end).toMinutes()
    }

    fun splitByDate(): List<MemberUsage> {
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
