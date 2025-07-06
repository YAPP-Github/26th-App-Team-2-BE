package com.yapp.brake.outbox.infrastructure.event

import com.yapp.brake.common.event.Event
import com.yapp.brake.common.event.EventPayload
import com.yapp.brake.common.event.EventType
import com.yapp.brake.outbox.model.Outbox
import com.yapp.brake.outbox.model.OutboxEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class OutboxEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun publish(
        type: EventType,
        payload: EventPayload,
    ) {
        val payload =
            Event.of(type, payload)
                .toJson() ?: throw IllegalStateException("serialize fail")

        val outbox = Outbox.create(type, payload)

        applicationEventPublisher.publishEvent(OutboxEvent.of(outbox))
    }
}
