package com.yapp.brake.session.utils

import java.time.LocalDate
import java.time.LocalDateTime

fun generateBetweenDates(
    startDate: LocalDate,
    endDate: LocalDate,
): List<LocalDate> {
    if (startDate > endDate) {
        return emptyList()
    }

    return generateSequence(startDate) { it.plusDays(1) }
        .takeWhile { it.isBefore(endDate) }
        .toList() + endDate
}

fun generateBetweenDates(
    startDate: LocalDateTime,
    endDate: LocalDateTime,
): List<LocalDate> {
    return generateBetweenDates(startDate.toLocalDate(), endDate.toLocalDate())
}
