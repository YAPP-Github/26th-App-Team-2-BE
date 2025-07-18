package com.yapp.brake.session.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.yapp.brake.session.validator.ValidSessionDate
import java.time.LocalDate

@ValidSessionDate
data class QuerySessionRequest(
    @JsonFormat(pattern = "yyyy-MM-dd")
    val start: LocalDate,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val end: LocalDate,
)
