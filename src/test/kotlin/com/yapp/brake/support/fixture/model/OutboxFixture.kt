package com.yapp.brake.support.fixture.model

import com.yapp.brake.common.event.Event
import com.yapp.brake.common.event.EventType
import com.yapp.brake.outbox.model.Outbox
import com.yapp.brake.support.fixture.event.FakeEventHandler
import com.yapp.brake.support.fixture.event.memberDeletedEventPayloadFixture
import java.time.LocalDateTime
import java.util.UUID

fun outboxFixture(
    outboxId: UUID = UUID.randomUUID(),
    handler: String = FakeEventHandler::class.java.name,
    eventType: EventType = FakeEventHandler().eventType,
    payload: String = Event.of(eventType, memberDeletedEventPayloadFixture()).toJson()!!,
    createdAt: LocalDateTime = LocalDateTime.now(),
) = Outbox(
    outboxId = outboxId.toString(),
    handler = handler,
    eventType = eventType,
    payload = payload,
    createdAt = createdAt,
)
