package com.yapp.brake.common.event.payload

import com.yapp.brake.common.event.EventPayload
import java.time.LocalDateTime

data class StatisticsUpdatedEventPayload(
    val memberId: Long,
    val groupId: Long,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val goalMinutes: Long,
) : EventPayload
