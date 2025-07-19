package com.yapp.brake.session.infrastructure.jpa.statistics

import com.yapp.brake.session.infrastructure.DailySessionStatisticsReader
import com.yapp.brake.session.model.DailySessionStatistics
import com.yapp.brake.session.model.SessionStatistics
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull

@Transactional
@Repository
class DailySessionStatisticsJpaReader(
    private val dailySessionStatisticsRepository: DailySessionStatisticsRepository,
) : DailySessionStatisticsReader {
    override fun getById(
        memberId: Long,
        date: LocalDate,
    ): DailySessionStatistics {
        return dailySessionStatisticsRepository.findById(DailySessionStatisticsId(memberId, date))
            .getOrNull()
            ?.toDomain()
            ?: DailySessionStatistics(memberId, date)
    }

    override fun getAllByIds(
        memberId: Long,
        dates: List<LocalDate>,
    ): SessionStatistics {
        val ids = dates.map { DailySessionStatisticsId(memberId, it) }
        val statistics = dailySessionStatisticsRepository.findAllById(ids).map { it.toDomain() }
        return SessionStatistics(statistics)
    }
}
