package com.yapp.brake.session.service

import com.yapp.brake.outbox.infrastructure.event.OutboxEventPublisher
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
    private val outboxEventPublisher = mock<OutboxEventPublisher>()
    private val sessionService = SessionService(sessionWriter, outboxEventPublisher)

    @Test
    fun `세션을 저장하고 통계 업데이트 이벤트를 발행한다`() {
        val session = sessionFixture()
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

        // when
        sessionService.add(
            session.deviceProfileId,
            request,
        )

        // then
        verify(sessionWriter).save(any())
        verify(outboxEventPublisher).publish(any(), any())
    }
}
