package com.yapp.brake.session.infrastructure

import com.yapp.brake.session.model.DailySessionStatistics

interface DailySessionStatisticsWriter {
    fun save(statistics: DailySessionStatistics): DailySessionStatistics
}
