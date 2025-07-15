package com.yapp.brake.session.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.time.LocalDate
import java.time.LocalTime

data class AddSessionRequest(
    @field:Positive
    val groupId: Long,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val date: LocalDate,
    @JsonFormat(pattern = "HH:mm")
    val startTime: LocalTime,
    @JsonFormat(pattern = "HH:mm")
    val endTime: LocalTime,
    @field:Positive
    val goalTime: Long,
    @field:PositiveOrZero
    val snoozeUnit: Int,
    @field:PositiveOrZero
    val snoozeCount: Int,
)
