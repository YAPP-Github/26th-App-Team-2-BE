package com.yapp.brake.support.fixture.model

import com.yapp.brake.session.model.Session
import com.yapp.brake.session.model.Snooze
import java.time.LocalDateTime

fun sessionFixture(
    sessionId: Long = 0L,
    groupId: Long = 1L,
    memberId: Long = 1L,
    start: LocalDateTime = LocalDateTime.now().minusMinutes(30),
    end: LocalDateTime = LocalDateTime.now(),
    goalTime: Long = 108000L,
    snoozeUnit: Int = 5,
    snoozeCount: Int = 0,
) = Session(
    id = sessionId,
    memberId = memberId,
    groupId = groupId,
    start = start,
    end = end,
    goalTime = goalTime,
    snooze =
        Snooze(
            count = snoozeCount,
            unit = snoozeUnit,
        ),
)
