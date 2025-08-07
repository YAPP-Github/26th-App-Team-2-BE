package com.yapp.brake.session.infrastructure.jpa

import com.yapp.brake.session.model.Session
import com.yapp.brake.session.model.Snooze
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "session")
class SessionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sessionId: Long = 0L,
    val groupId: Long,
    val deviceProfileId: Long,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val goalMinutes: Long,
    val snoozeUnit: Int,
    val snoozeCount: Int,
) {
    fun toDomain() =
        Session(
            id = sessionId,
            groupId = groupId,
            deviceProfileId = deviceProfileId,
            start = start,
            end = end,
            goalMinutes = goalMinutes,
            snooze =
                Snooze(
                    unit = snoozeUnit,
                    count = snoozeCount,
                ),
        )

    companion object {
        fun create(session: Session) =
            SessionEntity(
                sessionId = session.id,
                groupId = session.groupId,
                deviceProfileId = session.deviceProfileId,
                start = session.start,
                end = session.end,
                goalMinutes = session.goalMinutes,
                snoozeUnit = session.snooze.unit,
                snoozeCount = session.snooze.count,
            )
    }
}
