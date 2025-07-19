package com.yapp.brake.session.infrastructure.jpa.statistics

import java.io.Serializable
import java.time.LocalDate

data class DailySessionStatisticId(
    var memberId: Long = 0L,
    var date: LocalDate = LocalDate.MIN,
) : Serializable
