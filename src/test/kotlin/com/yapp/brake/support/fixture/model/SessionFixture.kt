package com.yapp.brake.support.fixture.model

import com.yapp.brake.session.model.Session
import com.yapp.brake.session.model.Snooze
import java.time.LocalDateTime

fun sessionFixture(
    sessionId: Long = 0L,
    groupId: Long = 1L,
    memberId: Long = 1L,
    start: LocalDateTime = LocalDateTime.of(2025, 7, 18, 0, 0, 0),
    end: LocalDateTime = start.plusMinutes(30),
    goalMinutes: Long = 30L,
    snoozeUnit: Int = 5,
    snoozeCount: Int = 0,
) = Session(
    id = sessionId,
    deviceProfileId = memberId,
    groupId = groupId,
    start = start,
    end = end,
    goalMinutes = goalMinutes,
    snooze =
        Snooze(
            count = snoozeCount,
            unit = snoozeUnit,
        ),
)
