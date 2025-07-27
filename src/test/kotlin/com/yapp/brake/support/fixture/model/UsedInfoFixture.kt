package com.yapp.brake.support.fixture.model

import com.yapp.brake.statistic.model.DeviceUsage
import java.time.LocalDateTime

fun memberUsageFixture(
    memberId: Long = 1L,
    start: LocalDateTime = LocalDateTime.of(2025, 7, 18, 0, 0, 0),
    end: LocalDateTime = start.plusMinutes(30),
    goalMinutes: Long = 30L,
) = DeviceUsage(
    deviceProfileId = memberId,
    start = start,
    end = end,
    goalMinutes = goalMinutes,
)
