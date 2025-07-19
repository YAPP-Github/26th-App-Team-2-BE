package com.yapp.brake.session.infrastructure.jpa.statistics

import com.yapp.brake.session.infrastructure.DailySessionStatisticReader
import com.yapp.brake.session.model.DailySessionStatistic
import com.yapp.brake.session.model.SessionStatistics
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
