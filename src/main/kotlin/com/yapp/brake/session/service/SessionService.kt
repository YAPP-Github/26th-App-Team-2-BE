package com.yapp.brake.session.service

import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.infrastructure.DailySessionStatisticsReader
import com.yapp.brake.session.infrastructure.DailySessionStatisticsWriter
import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.session.model.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SessionService(
    private val sessionWriter: SessionWriter,
    private val dailySessionStatisticsWriter: DailySessionStatisticsWriter,
    private val dailySessionStatisticsReader: DailySessionStatisticsReader,
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
                request.goalTime,
                request.snoozeUnit,
                request.snoozeCount,
            )
        val savedSession = sessionWriter.save(session)

        val statistics = dailySessionStatisticsReader.getById(memberId, request.start.toLocalDate())
        val updated = statistics.update(session)
        dailySessionStatisticsWriter.save(updated)

        return AddSessionResponse(savedSession.id)
    }
}
