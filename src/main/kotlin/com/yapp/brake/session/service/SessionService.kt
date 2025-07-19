package com.yapp.brake.session.service

import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.dto.response.DailySessionStatisticsResponse
import com.yapp.brake.session.dto.response.SessionStatisticsResponse
import com.yapp.brake.session.infrastructure.DailySessionStatisticsReader
import com.yapp.brake.session.infrastructure.DailySessionStatisticsWriter
import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.session.model.Session
import com.yapp.brake.session.utils.generateBetweenDates
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

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

        updateStatistics(memberId, session)
        return AddSessionResponse(savedSession.id)
    }

    private fun updateStatistics(
        memberId: Long,
        session: Session,
    ) {
        val betweenDates = generateBetweenDates(session.start, session.end)
        val statistics = dailySessionStatisticsReader.getAllByIds(memberId, betweenDates)
        val updated = statistics.update(session)
        dailySessionStatisticsWriter.saveAll(updated)
    }

    @Transactional(readOnly = true)
    override fun get(
        memberId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): SessionStatisticsResponse {
        val betweenDates = generateBetweenDates(startDate, endDate)
        val statistics = dailySessionStatisticsReader.getAllByIds(memberId, betweenDates)

        return SessionStatisticsResponse(
            statistics.statistics.map {
                DailySessionStatisticsResponse.create(
                    it.date,
                    it.actualMinutes,
                    it.goalMinutes,
                )
            },
        )
    }
}
