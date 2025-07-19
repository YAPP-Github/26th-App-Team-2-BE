package com.yapp.brake.session.infrastructure

import com.yapp.brake.session.model.DailySessionStatistics
import com.yapp.brake.session.model.SessionStatistics

interface DailySessionStatisticsWriter {
    fun save(statistics: DailySessionStatistics): DailySessionStatistics

    fun saveAll(statistics: SessionStatistics)
}
