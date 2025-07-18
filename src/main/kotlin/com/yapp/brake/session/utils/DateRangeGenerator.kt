package com.yapp.brake.session.utils

import java.time.LocalDate

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
