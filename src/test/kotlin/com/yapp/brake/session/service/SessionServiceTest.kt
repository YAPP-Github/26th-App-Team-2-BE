package com.yapp.brake.session.service

import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.infrastructure.DailySessionStatisticsReader
import com.yapp.brake.session.infrastructure.DailySessionStatisticsWriter
import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.support.fixture.model.dailySessionStatisticsFixture
import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SessionServiceTest {
    private val sessionWriter = mock<SessionWriter>()
    private val statisticsWriter = mock<DailySessionStatisticsWriter>()
    private val statisticsReader = mock<DailySessionStatisticsReader>()
    private val sessionService = SessionService(sessionWriter, statisticsWriter, statisticsReader)

    @Test
    fun `세션을 저장한다`() {
        val session = sessionFixture()
        val dailySessionStatistics = dailySessionStatisticsFixture()
        val request =
            AddSessionRequest(
                session.groupId,
                session.start,
                session.end,
                session.goalTime,
                session.snooze.unit,
                session.snooze.count,
            )

        whenever(sessionWriter.save(any())).thenReturn(session)
        whenever(statisticsReader.getById(any(), any())).thenReturn(dailySessionStatistics)

        // when
        sessionService.add(
            session.memberId,
            request,
        )

        // then
        verify(sessionWriter).save(any())
        verify(statisticsReader).getById(any(), any())
        verify(statisticsWriter).save(any())
    }
}
