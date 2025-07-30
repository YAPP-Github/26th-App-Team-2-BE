package com.yapp.brake.statistic.infrastructure

import com.yapp.brake.statistic.model.DailySessionStatistic
import com.yapp.brake.statistic.model.SessionStatistics

interface DailySessionStatisticWriter {
    fun save(statistics: DailySessionStatistic): DailySessionStatistic

    fun saveAll(statistics: SessionStatistics)
}
