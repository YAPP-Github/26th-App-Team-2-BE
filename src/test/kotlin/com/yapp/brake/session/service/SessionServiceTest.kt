package com.yapp.brake.session.service

import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SessionServiceTest {
    private val sessionWriter = mock<SessionWriter>()
    private val sessionService = SessionService(sessionWriter)

    @Test
    fun `세션을 저장한다`() {
        val session = sessionFixture()
        val request =
            AddSessionRequest(
                session.groupId,
                session.date,
                session.startTime,
                session.endTime,
                session.goalTime,
                session.snooze.unit,
                session.snooze.count,
            )

        whenever(sessionWriter.save(any())).thenReturn(session)

        // when
        sessionService.add(
            session.memberId,
            request,
        )

        // then
        verify(sessionWriter).save(session)
    }
}
