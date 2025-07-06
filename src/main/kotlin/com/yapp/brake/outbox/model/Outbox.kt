package com.yapp.brake.outbox.model

import com.yapp.brake.common.event.EventType
import java.time.LocalDateTime

data class Outbox(
    val outboxId: Long = 0L,
    val eventType: EventType,
    val payload: String,
    val createdAt: LocalDateTime? = null,
) {
    companion object {
        fun create(
            eventType: EventType,
            payload: String,
        ) = Outbox(
            eventType = eventType,
            payload = payload,
        )
    }
}
