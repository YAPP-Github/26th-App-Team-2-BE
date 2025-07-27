package com.yapp.brake.statistic.infrastructure.jpa

import com.yapp.brake.statistic.model.DailySessionStatistic
import com.yapp.brake.statistic.model.SessionStatistics
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
    val deviceProfileId: Long,
    @Id
    val date: LocalDate,
    val actualMinutes: Long,
    val goalMinutes: Long,
) {
    companion object {
        fun create(dailySessionStatistic: DailySessionStatistic): DailySessionStatisticEntity {
            return DailySessionStatisticEntity(
                deviceProfileId = dailySessionStatistic.deviceProfileId,
                date = dailySessionStatistic.date,
                actualMinutes = dailySessionStatistic.actualMinutes,
                goalMinutes = dailySessionStatistic.goalMinutes,
            )
        }

        fun create(sessionStatistics: SessionStatistics): List<DailySessionStatisticEntity> {
            return sessionStatistics.statistics.map {
                DailySessionStatisticEntity(
                    deviceProfileId = it.deviceProfileId,
                    date = it.date,
                    actualMinutes = it.actualMinutes,
                    goalMinutes = it.goalMinutes,
                )
            }
        }
    }

    fun toDomain(): DailySessionStatistic {
        return DailySessionStatistic(
            deviceProfileId = deviceProfileId,
            date = date,
            actualMinutes = actualMinutes,
            goalMinutes = goalMinutes,
        )
    }
}
