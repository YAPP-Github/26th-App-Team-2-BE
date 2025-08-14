package com.yapp.brake.support.fixture.dto.session

import com.yapp.brake.session.dto.request.AddSessionRequest
import java.time.LocalDateTime

fun addSessionRequestFixture(
    groupId: Long = 1L,
    start: LocalDateTime = LocalDateTime.of(2025, 8, 14, 10, 0),
    end: LocalDateTime = start.plusHours(1),
    goalMinutes: Long = 60L,
    snoozeUnit: Int = 5,
    snoozeCount: Int = 0,
): AddSessionRequest {
    return AddSessionRequest(
        groupId = groupId,
        start = start,
        end = end,
        goalMinutes = goalMinutes,
        snoozeUnit = snoozeUnit,
        snoozeCount = snoozeCount,
    )
}
