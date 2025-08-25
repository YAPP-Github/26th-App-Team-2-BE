package com.yapp.brake.session.utils

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class DateRangeGeneratorKtTest {
    @Test
    fun `시작과 끝이 다른 경우`() {
        val start = LocalDate.of(2025, 7, 1)
        val end = LocalDate.of(2025, 7, 4)

        val expected =
            listOf(
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 2),
                LocalDate.of(2025, 7, 3),
                LocalDate.of(2025, 7, 4),
            )

        val result = generateBetweenDates(start, end)
        assertEquals(expected, result)
    }

    @Test
    fun `시작과 끝이 같은 경우`() {
        val start = LocalDate.of(2025, 7, 1)
        val end = LocalDate.of(2025, 7, 1)

        val expected = listOf(LocalDate.of(2025, 7, 1))
        val result = generateBetweenDates(start, end)
        assertEquals(expected, result)
    }

    @Test
    fun `시작이 끝보다 이후인 경우`() {
        val start = LocalDate.of(2025, 7, 4)
        val end = LocalDate.of(2025, 7, 1)

        val expected = emptyList<LocalDate>()
        val result = generateBetweenDates(start, end)
        assertEquals(expected, result)
    }
}
