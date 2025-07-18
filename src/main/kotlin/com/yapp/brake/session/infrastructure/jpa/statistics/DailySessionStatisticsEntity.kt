package com.yapp.brake.session.infrastructure.jpa.statistics

import com.yapp.brake.session.model.DailySessionStatistics
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@IdClass(DailySessionStatisticsId::class)
@Table(name = "daily_session_statistics")
class DailySessionStatisticsEntity(
    @Id
    val memberId: Long,
    @Id
    val date: LocalDate,
    val actualTime: Long,
    val goalTime: Long,
) {
    companion object {
        fun create(dailySessionStatistics: DailySessionStatistics): DailySessionStatisticsEntity {
            return DailySessionStatisticsEntity(
                memberId = dailySessionStatistics.memberId,
                date = dailySessionStatistics.date,
                actualTime = dailySessionStatistics.actualTime,
                goalTime = dailySessionStatistics.goalTime,
            )
        }
    }

    fun toDomain(): DailySessionStatistics {
        return DailySessionStatistics(
            memberId = memberId,
            date = date,
            actualTime = actualTime,
            goalTime = goalTime,
        )
    }
}
