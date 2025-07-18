package com.yapp.brake.session.infrastructure.jpa.statistics

import com.yapp.brake.session.infrastructure.DailySessionStatisticsWriter
import com.yapp.brake.session.model.DailySessionStatistics
import org.springframework.stereotype.Repository

@Repository
class DailySessionStatisticsJpaWriter(
    private val dailySessionStatisticsRepository: DailySessionStatisticsRepository,
) : DailySessionStatisticsWriter {
    override fun save(statistics: DailySessionStatistics): DailySessionStatistics {
        val entity = DailySessionStatisticsEntity.create(statistics)
        return dailySessionStatisticsRepository.save(entity).toDomain()
    }
}
