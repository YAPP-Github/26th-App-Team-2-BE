package com.yapp.brake.session.service

import com.yapp.brake.session.dto.response.AddSessionResponse
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
        snoozeUnit: Int,
        snoozeCount: Int,
    ): AddSessionResponse
}
