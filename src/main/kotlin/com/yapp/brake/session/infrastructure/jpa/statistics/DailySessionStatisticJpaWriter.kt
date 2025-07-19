package com.yapp.brake.session.infrastructure.jpa.statistics

import com.yapp.brake.session.infrastructure.DailySessionStatisticWriter
import com.yapp.brake.session.model.DailySessionStatistic
import com.yapp.brake.session.model.SessionStatistics
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
