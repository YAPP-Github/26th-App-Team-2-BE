package com.yapp.brake.session.infrastructure

import com.yapp.brake.session.model.DailySessionStatistic
import com.yapp.brake.session.model.SessionStatistics

interface DailySessionStatisticWriter {
    fun save(statistics: DailySessionStatistic): DailySessionStatistic

    fun saveAll(statistics: SessionStatistics)
}
