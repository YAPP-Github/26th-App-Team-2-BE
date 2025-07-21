package com.yapp.brake.statistic.model

import com.yapp.brake.support.fixture.model.dailySessionStatisticsFixture
import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDateTime
import kotlin.test.assertEquals

class SessionStatisticsTest {
    @Test
    fun `날짜에 맞춰 통계를 업데이트한다`() {
        val start = LocalDateTime.of(2025, 7, 18, 23, 50)
        val session =
            sessionFixture(
                start = start,
                end = start.plusMinutes(40),
                goalMinutes = 30L,
            )
        val sessionStatistics = SessionStatistics(emptyList())

        // when
        val result = sessionStatistics.update(session)

        // then
        val startDate = start.toLocalDate()
        val expectedStatistics1 =
            dailySessionStatisticsFixture(
                memberId = 1L,
                date = startDate,
                actualMinutes = 10,
                goalMinutes = 10,
            )
        val expectedStatistics2 =
            dailySessionStatisticsFixture(
                memberId = 1L,
                date = startDate.plusDays(1),
                actualMinutes = 30,
                goalMinutes = 20,
            )

        assertAll(
            { assertEquals(2, result.statistics.size) },
            { assertEquals(expectedStatistics1, result.statistics.get(0)) },
            { assertEquals(expectedStatistics2, result.statistics.get(1)) },
        )
    }
}
