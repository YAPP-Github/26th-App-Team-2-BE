package com.yapp.brake.statistic.dto.response

import com.yapp.brake.support.fixture.model.dailySessionStatisticsFixture
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DailySessionStatisticResponseTest {
    @Test
    fun `날짜와 분 단위를 받아 요일과 시간을 만든다`() {
        // given
        val date = LocalDate.of(2025, 7, 19)
        val dailySessionStatisticsFixture =
            dailySessionStatisticsFixture(
                date = date,
                actualMinutes = 90L,
                goalMinutes = 120L,
            )

        // when
        val response = DailySessionStatisticResponse.from(dailySessionStatisticsFixture)

        // then
        assertEquals(date, response.date)
        assertEquals(DayOfWeek.SATURDAY, response.dayOfWeek)
        assertEquals(LocalTime.of(1, 30), response.actualTime)
        assertEquals(LocalTime.of(2, 0), response.goalTime)
    }
}
