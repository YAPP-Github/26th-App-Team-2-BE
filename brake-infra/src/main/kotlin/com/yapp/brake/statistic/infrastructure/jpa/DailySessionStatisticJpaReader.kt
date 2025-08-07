package com.yapp.brake.statistic.infrastructure.jpa

import com.yapp.brake.statistic.infrastructure.DailySessionStatisticReader
import com.yapp.brake.statistic.model.DailySessionStatistic
import com.yapp.brake.statistic.model.SessionStatistics
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull

@Transactional
@Repository
class DailySessionStatisticJpaReader(
    private val dailySessionStatisticRepository: DailySessionStatisticRepository,
) : DailySessionStatisticReader {
    override fun getById(
        deviceProfileId: Long,
        date: LocalDate,
    ): DailySessionStatistic {
        return dailySessionStatisticRepository.findById(DailySessionStatisticId(deviceProfileId, date))
            .getOrNull()
            ?.toDomain()
            ?: DailySessionStatistic(deviceProfileId, date)
    }

    override fun getAllByIds(
        deviceProfileId: Long,
        dates: List<LocalDate>,
    ): SessionStatistics {
        val ids = dates.map { DailySessionStatisticId(deviceProfileId, it) }
        val statistics = dailySessionStatisticRepository.findAllById(ids).map { it.toDomain() }
        return SessionStatistics(statistics)
    }
}
