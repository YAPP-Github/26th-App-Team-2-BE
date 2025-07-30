package com.yapp.brake.statistic.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.yapp.brake.statistic.validator.ValidSessionDate
import jakarta.annotation.Nullable
import java.time.LocalDate

@ValidSessionDate
data class QueryStatisticRequest(
    @field:Nullable
    @JsonFormat(pattern = "yyyy-MM-dd")
    private val start: LocalDate?,
    @field:Nullable
    @JsonFormat(pattern = "yyyy-MM-dd")
    private val end: LocalDate?,
) {
    fun getStartOrDefault(): LocalDate {
        return start ?: (end?.minusDays(6) ?: LocalDate.now().minusDays(6))
    }

    fun getEndOrDefault(): LocalDate {
        return end ?: (start?.plusDays(6) ?: LocalDate.now())
    }
}
