package com.yapp.brake.common.event

import com.yapp.brake.common.serializer.DataSerializer.deserialize
import com.yapp.brake.common.serializer.DataSerializer.serialize

class Event<T : EventPayload>(
    val type: EventType,
    val payload: T,
) {
    fun toJson() = serialize(this)

    companion object {
        fun of(
            type: EventType,
            payload: EventPayload,
        ) = Event(
            type = type,
            payload = payload,
        )

        fun fromJson(json: String?): Event<EventPayload>? {
            val eventRaw =
                deserialize(json, EventRaw::class.java)
                    ?: return null

            val eventType = EventType.from(eventRaw.type)
            val payloadClass = eventType.payloadClass
            val payload = deserialize(eventRaw.payload, payloadClass)

            return of(eventType, payload)
        }

        private data class EventRaw(
            val type: String,
            val payload: Any,
        )
    }
}
