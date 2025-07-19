package com.yapp.brake.common.event

import com.yapp.brake.common.event.payload.GroupDeletedEventPayload
import com.yapp.brake.common.event.payload.GroupUpdatedEventPayload
import com.yapp.brake.common.event.payload.MemberDeletedEventPayload
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger { }

enum class EventType(
    val payloadClass: Class<out EventPayload>,
) {
    MEMBER_DELETED(MemberDeletedEventPayload::class.java),
    GROUP_UPDATED(GroupUpdatedEventPayload::class.java),
    GROUP_DELETED(GroupDeletedEventPayload::class.java),
    ;

    companion object {
        fun from(type: String): EventType {
            try {
                return valueOf(type)
            } catch (e: Exception) {
                logger.error(e) { "[EventType.from] type=$type" }
                throw IllegalArgumentException("Invalid type=$type")
            }
        }
    }
}
