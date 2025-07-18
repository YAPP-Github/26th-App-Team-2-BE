package com.yapp.brake.session.infrastructure.jpa

import com.yapp.brake.session.infrastructure.DailySessionStatisticsReader
import com.yapp.brake.session.model.DailySessionStatistics
import org.springframework.stereotype.Repository
import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull

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
}
