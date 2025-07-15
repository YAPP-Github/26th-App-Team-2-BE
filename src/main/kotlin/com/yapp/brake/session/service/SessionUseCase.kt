package com.yapp.brake.session.service

import com.yapp.brake.session.model.Snooze
import java.time.LocalDate
import java.time.LocalTime

interface SessionUseCase {
    fun add(
        memberId: Long,
        groupId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        goalTime: Long,
        snooze: Snooze,
    )
}
