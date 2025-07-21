package com.yapp.brake.session.service

import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.session.model.Session
import com.yapp.brake.session.utils.generateBetweenDates
import com.yapp.brake.statistic.infrastructure.DailySessionStatisticReader
import com.yapp.brake.statistic.infrastructure.DailySessionStatisticWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SessionService(
    private val sessionWriter: SessionWriter,
    private val dailySessionStatisticWriter: DailySessionStatisticWriter,
    private val dailySessionStatisticReader: DailySessionStatisticReader,
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

        updateStatistics(memberId, session)
        return AddSessionResponse(savedSession.id)
    }

    private fun updateStatistics(
        memberId: Long,
        session: Session,
    ) {
        val betweenDates = generateBetweenDates(session.start, session.end)
        val statistics = dailySessionStatisticReader.getAllByIds(memberId, betweenDates)
        val updated = statistics.update(session)
        dailySessionStatisticWriter.saveAll(updated)
    }
}
