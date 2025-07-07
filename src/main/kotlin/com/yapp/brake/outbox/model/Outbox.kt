package com.yapp.brake.outbox.model

import com.yapp.brake.common.event.EventType
import java.time.LocalDateTime
import java.util.UUID

data class Outbox(
    val outboxId: String,
    val eventType: EventType,
    val payload: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun create(
            eventType: EventType,
            payload: String,
        ) = Outbox(
            outboxId = UUID.randomUUID().toString(),
            eventType = eventType,
            payload = payload,
            createdAt = LocalDateTime.now(),
        )
    }
}
