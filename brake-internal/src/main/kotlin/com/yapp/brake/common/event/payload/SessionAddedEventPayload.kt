package com.yapp.brake.common.event.payload

import com.yapp.brake.common.event.EventPayload
import java.time.LocalDateTime

data class SessionAddedEventPayload(
    val deviceProfileId: Long,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val goalMinutes: Long,
) : EventPayload
