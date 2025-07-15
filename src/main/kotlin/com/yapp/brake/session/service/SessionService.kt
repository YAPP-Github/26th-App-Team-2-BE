package com.yapp.brake.session.service

import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.session.model.Session
import com.yapp.brake.session.model.Snooze
import java.time.LocalDate
import java.time.LocalTime

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
        snooze: Snooze,
    ) {
        val session =
            Session.create(
                memberId,
                groupId,
                date,
                startTime,
                endTime,
                goalTime,
                snooze,
            )
        sessionWriter.save(session)
    }
}
