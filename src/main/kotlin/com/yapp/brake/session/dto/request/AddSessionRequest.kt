package com.yapp.brake.session.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.time.LocalDateTime

data class AddSessionRequest(
    @field:Positive
    val groupId: Long,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val start: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val end: LocalDateTime,
    @field:Positive
    val goalTime: Long,
    @field:PositiveOrZero
    val snoozeUnit: Int,
    @field:PositiveOrZero
    val snoozeCount: Int,
)
