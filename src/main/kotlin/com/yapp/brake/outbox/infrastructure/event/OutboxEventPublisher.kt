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
    private val handlerMap = eventHandlers.associateBy { it::class.java.name }

    fun publish(
        type: EventType,
        payload: EventPayload,
    ) {
        val event =
            Event.of(type, payload)
                .toJson() ?: throw IllegalStateException("serialize fail")

        val outboxes =
            handlerMap.filter { (_, handler) -> handler.getEventType() == type }
                .map { (handlerName, _) -> Outbox.create(handlerName, type, event) }

        outboxes.forEach { applicationEventPublisher.publishEvent(OutboxEvent.of(it)) }
    }
}
