package com.yapp.brake.session.service

import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.infrastructure.DailySessionStatisticReader
import com.yapp.brake.session.infrastructure.DailySessionStatisticWriter
import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.session.model.SessionStatistics
import com.yapp.brake.support.fixture.model.dailySessionStatisticsFixture
import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyList
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.assertEquals

class SessionServiceTest {
    private val sessionWriter = mock<SessionWriter>()
    private val statisticsWriter = mock<DailySessionStatisticWriter>()
    private val statisticsReader = mock<DailySessionStatisticReader>()
    private val sessionService = SessionService(sessionWriter, statisticsWriter, statisticsReader)

    @Test
    fun `세션을 저장한다`() {
        val session = sessionFixture()
        val dailySessionStatistics = dailySessionStatisticsFixture()
        val sessionStatistics = SessionStatistics(listOf(dailySessionStatistics))
        val request =
            AddSessionRequest(
                session.groupId,
                session.start,
                session.end,
                session.goalMinutes,
                session.snooze.unit,
                session.snooze.count,
            )

        whenever(sessionWriter.save(any())).thenReturn(session)
        whenever(statisticsReader.getAllByIds(any(), anyList())).thenReturn(sessionStatistics)

        // when
        sessionService.add(
            session.memberId,
            request,
        )

        // then
        verify(sessionWriter).save(any())
        verify(statisticsReader).getAllByIds(any(), anyList())
        verify(statisticsWriter).saveAll(any())
    }

    @Test
    fun `세션을 조회한다`() {
        val today = LocalDate.now()
        val sessionStatistics = SessionStatistics(listOf())

        whenever(statisticsReader.getAllByIds(any(), anyList())).thenReturn(sessionStatistics)

        // when
        sessionService.get(
            1L,
            today.minusDays(1),
            today,
        )

        // then
        verify(statisticsReader).getAllByIds(any(), anyList())
    }

    @Test
    fun `통계 조회시 DailySessionStatisticsResponse 리스트를 반환한다`() {
        // given
        val memberId = 1L
        val startDate = LocalDate.of(2025, 7, 18)
        val endDate = LocalDate.of(2025, 7, 19)

        val betweenDates = listOf(startDate, endDate)

        val mockStatistics =
            listOf(
                dailySessionStatisticsFixture(
                    memberId = memberId,
                    date = startDate,
                    actualMinutes = 50,
                    goalMinutes = 60,
                ),
                dailySessionStatisticsFixture(
                    memberId = memberId,
                    date = endDate,
                    actualMinutes = 30,
                    goalMinutes = 40,
                ),
            )

        whenever(statisticsReader.getAllByIds(memberId, betweenDates))
            .thenReturn(SessionStatistics(mockStatistics))

        // when
        val result = sessionService.get(memberId, startDate, endDate)

        // then
        assertEquals(2, result.statistics.size)
        assertEquals(startDate, result.statistics[0].date)
        assertEquals(LocalTime.of(0, 50), result.statistics[0].actualTime)
        assertEquals(LocalTime.of(1, 0), result.statistics[0].goalTime)

        assertEquals(endDate, result.statistics[1].date)
        assertEquals(LocalTime.of(0, 30), result.statistics[1].actualTime)
        assertEquals(LocalTime.of(0, 40), result.statistics[1].goalTime)
    }
}
