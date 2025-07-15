package com.yapp.brake.session.dto.request

import java.time.LocalDate
import java.time.LocalTime

data class AddSessionRequest(
    val groupId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val goalTime: Long,
    val snoozeUnit: Int = 0,
    val snoozeCount: Int = 0,
)
