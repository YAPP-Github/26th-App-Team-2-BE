package com.yapp.brake.outbox.infrastructure.event

import com.yapp.brake.common.event.Event
import com.yapp.brake.common.event.EventPayload
import com.yapp.brake.common.event.EventPayloadHandler
import com.yapp.brake.common.event.EventType
import com.yapp.brake.outbox.model.Outbox
import com.yapp.brake.outbox.model.OutboxEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class OutboxEventPublisher(
    private val eventHandlers: List<EventPayloadHandler<*>>,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    private val handlerMap: Map<EventType, List<EventPayloadHandler<EventPayload>>> =
        eventHandlers.filterIsInstance<EventPayloadHandler<EventPayload>>()
            .groupBy { it.eventType }

    fun publish(
        type: EventType,
        payload: EventPayload,
    ) {
        val event =
            Event.of(type, payload)
                .toJson() ?: throw IllegalStateException("serialize fail")

        val outboxes =
            handlerMap[type].orEmpty()
                .map { handler -> Outbox.create(handler::class.java.name, type, event) }

        outboxes.forEach { applicationEventPublisher.publishEvent(OutboxEvent.of(it)) }
    }
}
