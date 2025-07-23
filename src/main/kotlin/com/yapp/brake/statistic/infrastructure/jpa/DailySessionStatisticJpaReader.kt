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
        memberId: Long,
        date: LocalDate,
    ): DailySessionStatistic {
        return dailySessionStatisticRepository.findById(DailySessionStatisticId(memberId, date))
            .getOrNull()
            ?.toDomain()
            ?: DailySessionStatistic(memberId, date)
    }

    override fun getAllByIds(
        memberId: Long,
        dates: List<LocalDate>,
    ): SessionStatistics {
        val ids = dates.map { DailySessionStatisticId(memberId, it) }
        val statistics = dailySessionStatisticRepository.findAllById(ids).map { it.toDomain() }
        return SessionStatistics(statistics)
    }
}
