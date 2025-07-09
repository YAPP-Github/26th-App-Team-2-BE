package com.yapp.brake.outbox.infrastructure.jpa

import com.yapp.brake.common.event.EventType
import com.yapp.brake.outbox.model.Outbox
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "outbox")
@Entity
class OutboxEntity(
    @Id
    val outboxId: String,
    val handler: String,
    @Enumerated(EnumType.STRING)
    val eventType: EventType,
    val payload: String,
    val createdAt: LocalDateTime,
) {
    fun toDomain() =
        Outbox(
            outboxId = outboxId,
            handler = handler,
            eventType = eventType,
            payload = payload,
            createdAt = createdAt,
        )

    companion object {
        fun from(outbox: Outbox) =
            OutboxEntity(
                outboxId = outbox.outboxId,
                handler = outbox.handler,
                eventType = outbox.eventType,
                payload = outbox.payload,
                createdAt = outbox.createdAt,
            )
    }
}
