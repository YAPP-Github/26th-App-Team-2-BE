package com.yapp.brake.statistic.jpa

import java.io.Serializable
import java.time.LocalDate

data class DailySessionStatisticId(
    var deviceProfileId: Long = 0L,
    var date: LocalDate = LocalDate.MIN,
) : Serializable
