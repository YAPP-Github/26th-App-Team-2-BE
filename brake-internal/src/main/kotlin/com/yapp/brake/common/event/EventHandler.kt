package com.yapp.brake.common.event

interface EventHandler<in T : EventPayload> {
    val eventType: EventType

    fun handle(payload: T)
}
