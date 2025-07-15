package com.yapp.brake.support.fixture.model

import com.yapp.brake.session.model.Session
import com.yapp.brake.session.model.Snooze
import java.time.LocalDate
import java.time.LocalTime

fun sessionFixture(
    sessionId: Long = 0L,
    groupId: Long = 1L,
    memberId: Long = 1L,
    date: LocalDate = LocalDate.now(),
    startTime: LocalTime = LocalTime.now().minusMinutes(30),
    endTime: LocalTime = LocalTime.now(),
    goalTime: Long = 108000L,
    snoozeUnit: Int = 5,
    snoozeCount: Int = 0,
) = Session(
    id = sessionId,
    memberId = memberId,
    groupId = groupId,
    date = date,
    startTime = startTime,
    endTime = endTime,
    goalTime = goalTime,
    snooze =
        Snooze(
            count = snoozeCount,
            unit = snoozeUnit,
        ),
)
