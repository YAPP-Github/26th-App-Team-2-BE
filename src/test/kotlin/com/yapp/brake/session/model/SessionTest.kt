package com.yapp.brake.session.model

import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals

class SessionTest {
    @Test
    fun `실제 사용 시간을 계산한다`() {
        // given
        val usageMinutes = 30L
        val start = LocalDateTime.of(2025, 7, 18, 12, 0, 0)
        val end = start.plusMinutes(usageMinutes)
        val session =
            sessionFixture(
                start = start,
                end = end,
            )

        // when
        val actual = session.toActualMinutes()

        // then
        assertEquals(usageMinutes, actual)
    }

    @Test
    fun `자정에 걸친 세션 날짜를 분리한다`() {
        // given
        val start = LocalDateTime.of(2025, 7, 18, 23, 50, 0)
        val end = LocalDateTime.of(2025, 7, 19, 0, 20, 0)
        val session =
            sessionFixture(
                start = start,
                end = end,
                goalMinutes = 20,
            )

        // when
        val actual = session.splitByDate()

        // then
        val sessions1 =
            session.copy(
                start = start,
                end = LocalDate.of(2025, 7, 19).atStartOfDay(),
                goalMinutes = 10L,
            )
        val sessions2 =
            session.copy(
                start = LocalDate.of(2025, 7, 19).atStartOfDay(),
                end = end,
                goalMinutes = 10L,
            )

        assertAll(
            { assertEquals(2, actual.size) },
            { assertEquals(sessions1, actual.get(0)) },
            { assertEquals(sessions2, actual.get(1)) },
        )
    }
}
