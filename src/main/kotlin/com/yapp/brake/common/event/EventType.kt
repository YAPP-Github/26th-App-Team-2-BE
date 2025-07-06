package com.yapp.brake.common.event

import com.yapp.brake.common.event.payload.AuthWithdrawEventPayload
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger { }

enum class EventType(
    val payloadClass: Class<out EventPayload>,
) {
    AUTH_WITHDRAW(AuthWithdrawEventPayload::class.java),
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
