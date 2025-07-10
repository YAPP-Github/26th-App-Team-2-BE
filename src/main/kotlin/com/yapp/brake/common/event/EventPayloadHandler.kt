package com.yapp.brake.common.event

interface EventPayloadHandler<in T : EventPayload> {
    val eventType: EventType

    fun handle(payload: T)
}
