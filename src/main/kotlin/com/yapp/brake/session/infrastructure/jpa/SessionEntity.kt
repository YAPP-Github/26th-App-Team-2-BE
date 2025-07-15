package com.yapp.brake.session.infrastructure.jpa

import com.yapp.brake.common.persistence.Auditable
import com.yapp.brake.session.model.Session
import com.yapp.brake.session.model.Snooze
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "session")
class SessionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sessionId: Long = 0L,
    val groupId: Long,
    val memberId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val goalTime: Long,
    val snoozeUnit: Int = 0,
    val snoozeCount: Int = 0,
) : Auditable() {
    fun toDomain() =
        Session(
            id = sessionId,
            groupId = groupId,
            memberId = memberId,
            date = date,
            startTime = startTime,
            endTime = endTime,
            goalTime = goalTime,
            snooze =
                Snooze(
                    count = snoozeCount,
                    unit = snoozeUnit,
                ),
        )

    companion object {
        fun create(session: Session) =
            SessionEntity(
                sessionId = session.id,
                groupId = session.groupId,
                memberId = session.memberId,
                date = session.date,
                startTime = session.startTime,
                endTime = session.endTime,
                goalTime = session.goalTime,
                snoozeUnit = session.snooze.unit,
                snoozeCount = session.snooze.count,
            )
    }
}
