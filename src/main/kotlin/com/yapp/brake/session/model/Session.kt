package com.yapp.brake.session.model

import java.time.LocalDate
import java.time.LocalTime

data class Session(
    val id: Long = 0L,
    val groupId: Long,
    val memberId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val goalTime: Long,
    val snooze: Snooze,
)
