package com.yapp.brake.statistic.infrastructure.jpa

import com.yapp.brake.statistic.model.DailySessionStatistic
import com.yapp.brake.statistic.model.SessionStatistics
import org.springframework.stereotype.Repository

@Repository
class DailySessionStatisticJpaWriter(
    private val dailySessionStatisticRepository: DailySessionStatisticRepository,
) : DailySessionStatisticWriter {
    override fun save(statistics: DailySessionStatistic): DailySessionStatistic {
        val entity = DailySessionStatisticEntity.create(statistics)
        return dailySessionStatisticRepository.save(entity).toDomain()
    }

    override fun saveAll(statistics: SessionStatistics) {
        val entities = DailySessionStatisticEntity.create(statistics)
        dailySessionStatisticRepository.saveAll(entities)
    }
}
