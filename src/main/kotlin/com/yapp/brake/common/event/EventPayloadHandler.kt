package com.yapp.brake.common.event

interface EventPayloadHandler<T : EventPayload> {
    fun handle(payload: T)

    fun getEventType(): EventType
}
