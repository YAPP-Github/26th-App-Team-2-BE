package com.yapp.brake.statistic.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.yapp.brake.statistic.validator.ValidSessionDate
import jakarta.annotation.Nullable
import java.time.LocalDate

@ValidSessionDate
data class QueryStatisticRequest(
    @field:Nullable
    @JsonFormat(pattern = "yyyy-MM-dd")
    val start: LocalDate = LocalDate.now().minusDays(6),
    @field:Nullable
    @JsonFormat(pattern = "yyyy-MM-dd")
    val end: LocalDate = LocalDate.now(),
)
