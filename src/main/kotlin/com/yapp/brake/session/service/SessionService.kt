package com.yapp.brake.session.service

import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.session.model.Session
import org.springframework.stereotype.Service

@Service
class SessionService(
    private val sessionWriter: SessionWriter,
) : SessionUseCase {
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
                request.goalTime,
                request.snoozeUnit,
                request.snoozeCount,
            )
        val savedSession = sessionWriter.save(session)
        return AddSessionResponse(savedSession.id)
    }
}
