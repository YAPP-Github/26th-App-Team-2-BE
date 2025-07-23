package com.yapp.brake.session.service

import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.SessionAddedEventPayload
import com.yapp.brake.outbox.infrastructure.event.OutboxEventPublisher
import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.session.model.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SessionService(
    private val sessionWriter: SessionWriter,
    private val outboxEventPublisher: OutboxEventPublisher,
) : SessionUseCase {
    @Transactional
    override fun add(
        memberId: Long,
        request: AddSessionRequest,
    ): AddSessionResponse {
        val session =
            Session.create(
                memberId,
                request.groupId,
                request.start,
                request.end,
                request.goalMinutes,
                request.snoozeUnit,
                request.snoozeCount,
            )
        val savedSession = sessionWriter.save(session)

        val payload =
            SessionAddedEventPayload(
                memberId = memberId,
                start = session.start,
                end = session.end,
                goalMinutes = request.goalMinutes,
            )
        outboxEventPublisher.publish(EventType.SESSION_ADDED, payload)

        return AddSessionResponse.from(savedSession.id)
    }
}
