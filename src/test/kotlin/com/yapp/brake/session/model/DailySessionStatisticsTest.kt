package com.yapp.brake.session.model

import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals

class DailySessionStatisticsTest {
    private val start = LocalDateTime.of(2025, 7, 18, 12, 0, 0)
    private val end = LocalDateTime.of(2025, 7, 18, 12, 30, 0)
    private val goalTime = 1800L

    @Test
    fun `실제 사용 시간과 목표 사용 시간이 누적된다`() {
        // given
        val memberId = 1L
        val date = LocalDate.of(2025, 7, 18)
        val origin = DailySessionStatistics(memberId, date, actualTime = 600L, goalTime = 600L)
        val session =
            sessionFixture(
                start = start,
                end = end,
                goalTime = goalTime,
            )

        // when
        val updated = origin.update(session)

        // then
        assertEquals(origin.actualTime + (30 * 60L), updated.actualTime)
        assertEquals(origin.goalTime + goalTime, updated.goalTime)
        assertEquals(memberId, updated.memberId)
        assertEquals(date, updated.date)
    }
}
