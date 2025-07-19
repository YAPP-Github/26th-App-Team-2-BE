package com.yapp.brake.session.model

import com.yapp.brake.support.fixture.model.dailySessionStatisticsFixture
import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDateTime
import kotlin.test.assertEquals

class DailySessionStatisticsTest {
    private val goalMinutes = 30L
    private val usageMinutes = 10L
    private val start = LocalDateTime.of(2025, 7, 18, 12, 0, 0)
    private val end = start.plusMinutes(usageMinutes)

    @Test
    fun `실제 사용 시간과 목표 사용 시간이 누적된다`() {
        // given
        val memberId = 1L
        val date = start.toLocalDate()
        val originStatistics =
            dailySessionStatisticsFixture(
                memberId = memberId,
                date = date,
                actualMinutes = 10L,
                goalMinutes = 10L,
            )
        val session =
            sessionFixture(
                start = start,
                end = end,
                goalMinutes = goalMinutes,
            )

        // when
        val updated = originStatistics.add(session)

        // then
        assertAll(
            { assertEquals(originStatistics.actualMinutes + usageMinutes, updated.actualMinutes) },
            { assertEquals(originStatistics.goalMinutes + goalMinutes, updated.goalMinutes) },
            { assertEquals(memberId, updated.memberId) },
            { assertEquals(date, updated.date) },
        )
    }

    @Test
    fun `여러 날짜 중 일치하는 날짜의 데이터만 누적된다`() {
        // given
        val memberId = 1L
        val date = start.toLocalDate()
        val originStatistics =
            dailySessionStatisticsFixture(
                memberId = memberId,
                date = date,
                actualMinutes = 0L,
                goalMinutes = 0L,
            )
        val session =
            sessionFixture(
                start = start,
                end = end,
                goalMinutes = goalMinutes,
            )

        // when
        val updated = originStatistics.add(session)

        // then
        assertAll(
            { assertEquals(originStatistics.actualMinutes + usageMinutes, updated.actualMinutes) },
            { assertEquals(originStatistics.goalMinutes + goalMinutes, updated.goalMinutes) },
            { assertEquals(memberId, updated.memberId) },
            { assertEquals(date, updated.date) },
        )
    }

    @Test
    fun `일치하는 날짜가 없다면 그대로 반환한다`() {
        // given
        val memberId = 1L
        val date = start.toLocalDate()
        val originStatistics =
            dailySessionStatisticsFixture(
                memberId = memberId,
                date = date,
                actualMinutes = 0L,
                goalMinutes = 0L,
            )
        val session =
            sessionFixture(
                start = start.plusDays(1),
                goalMinutes = goalMinutes,
            )

        // when
        val updated = originStatistics.add(session)

        // then
        assertAll(
            { assertEquals(originStatistics.actualMinutes, updated.actualMinutes) },
            { assertEquals(originStatistics.goalMinutes, updated.goalMinutes) },
            { assertEquals(memberId, updated.memberId) },
            { assertEquals(date, updated.date) },
        )
    }
}
