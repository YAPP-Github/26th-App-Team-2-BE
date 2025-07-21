package com.yapp.brake.statistic.service.eventhandler

import com.yapp.brake.common.event.payload.StatisticsUpdatedEventPayload
import com.yapp.brake.statistic.infrastructure.DailySessionStatisticReader
import com.yapp.brake.statistic.infrastructure.DailySessionStatisticWriter
import com.yapp.brake.statistic.model.SessionStatistics
import com.yapp.brake.support.fixture.model.dailySessionStatisticsFixture
import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class StatisticsUpdatedEventHandlerTest {
    private val dailySessionStatisticReader: DailySessionStatisticReader = mock<DailySessionStatisticReader>()
    private val dailySessionStatisticWriter: DailySessionStatisticWriter = mock<DailySessionStatisticWriter>()
    private val statisticsUpdatedEventHandler =
        StatisticsUpdatedEventHandler(dailySessionStatisticReader, dailySessionStatisticWriter)

    val session = sessionFixture()
    val payload =
        StatisticsUpdatedEventPayload(
            memberId = session.memberId,
            start = session.start,
            end = session.end,
            groupId = session.groupId,
            goalMinutes = session.goalMinutes,
        )

    @Test
    fun `통계를 누적 업데이트한다`() {
        // given
        val sessionStatistics = SessionStatistics(listOf(dailySessionStatisticsFixture()))
        whenever(dailySessionStatisticReader.getAllByIds(any(), any())).thenReturn(sessionStatistics)

        // when
        statisticsUpdatedEventHandler.handle(payload)

        // then
        verify(dailySessionStatisticReader).getAllByIds(any(), any())
        verify(dailySessionStatisticWriter).saveAll(any())
    }

    @Test
    fun `기존 통계가 없더라도 업데이트한다`() {
        // given
        val sessionStatistics = SessionStatistics(listOf())
        whenever(dailySessionStatisticReader.getAllByIds(any(), any())).thenReturn(sessionStatistics)

        // when
        statisticsUpdatedEventHandler.handle(payload)

        // then
        verify(dailySessionStatisticReader).getAllByIds(any(), any())
        verify(dailySessionStatisticWriter).saveAll(any())
    }
}
