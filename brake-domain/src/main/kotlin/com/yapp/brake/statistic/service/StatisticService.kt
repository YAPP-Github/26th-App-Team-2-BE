package com.yapp.brake.statistic.service

import com.yapp.brake.session.utils.generateBetweenDates
import com.yapp.brake.statistic.dto.response.SessionStatisticsResponse
import com.yapp.brake.statistic.infrastructure.DailySessionStatisticReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
internal class StatisticService(
    private val dailySessionStatisticReader: DailySessionStatisticReader,
) : StatisticUseCase {
    @Transactional(readOnly = true)
    override fun get(
        deviceProfileId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): SessionStatisticsResponse {
        val betweenDates = generateBetweenDates(startDate, endDate)
        val statistics = dailySessionStatisticReader.getAllByIds(deviceProfileId, betweenDates)

        return SessionStatisticsResponse.from(statistics)
    }
}
