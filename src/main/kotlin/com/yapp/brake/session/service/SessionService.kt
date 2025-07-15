package com.yapp.brake.session.service

import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.session.model.Session
import com.yapp.brake.session.model.Snooze
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class SessionService(
    private val sessionWriter: SessionWriter,
) : SessionUseCase {
    override fun add(
        memberId: Long,
        groupId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        goalTime: Long,
        snoozeUnit: Int,
        snoozeCount: Int,
    ): AddSessionResponse {
        val session =
            Session.create(
                memberId,
                groupId,
                date,
                startTime,
                endTime,
                goalTime,
                Snooze(
                    snoozeCount,
                    snoozeUnit,
                ),
            )
        val savedSession = sessionWriter.save(session)
        return AddSessionResponse(savedSession.id)
    }
}
