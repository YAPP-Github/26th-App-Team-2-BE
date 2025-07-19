package com.yapp.brake.session.infrastructure.jpa.statistics

import com.yapp.brake.session.model.DailySessionStatistic
import com.yapp.brake.session.model.SessionStatistics
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@IdClass(DailySessionStatisticId::class)
@Table(name = "daily_session_statistic")
class DailySessionStatisticEntity(
    @Id
    val memberId: Long,
    @Id
    val date: LocalDate,
    val actualMinutes: Long,
    val goalMinutes: Long,
) {
    companion object {
        fun create(dailySessionStatistic: DailySessionStatistic): DailySessionStatisticEntity {
            return DailySessionStatisticEntity(
                memberId = dailySessionStatistic.memberId,
                date = dailySessionStatistic.date,
                actualMinutes = dailySessionStatistic.actualMinutes,
                goalMinutes = dailySessionStatistic.goalMinutes,
            )
        }

        fun create(sessionStatistics: SessionStatistics): List<DailySessionStatisticEntity> {
            return sessionStatistics.statistics.map {
                DailySessionStatisticEntity(
                    memberId = it.memberId,
                    date = it.date,
                    actualMinutes = it.actualMinutes,
                    goalMinutes = it.goalMinutes,
                )
            }
        }
    }

    fun toDomain(): DailySessionStatistic {
        return DailySessionStatistic(
            memberId = memberId,
            date = date,
            actualMinutes = actualMinutes,
            goalMinutes = goalMinutes,
        )
    }
}
