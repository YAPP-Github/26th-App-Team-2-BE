package com.yapp.brake.session.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.yapp.brake.session.validator.ValidSessionDate
import jakarta.annotation.Nullable
import java.time.LocalDate

@ValidSessionDate
data class QuerySessionRequest(
    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd")
    val start: LocalDate = LocalDate.now().minusDays(6),
    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd")
    val end: LocalDate = LocalDate.now(),
)
