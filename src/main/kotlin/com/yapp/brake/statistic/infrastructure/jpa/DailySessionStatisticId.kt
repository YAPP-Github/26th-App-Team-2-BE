package com.yapp.brake.statistic.infrastructure.jpa

import java.io.Serializable
import java.time.LocalDate

data class DailySessionStatisticId(
    var memberId: Long = 0L,
    var date: LocalDate = LocalDate.MIN,
) : Serializable
