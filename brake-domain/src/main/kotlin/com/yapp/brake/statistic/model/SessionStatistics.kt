package com.yapp.brake.statistic.model

data class SessionStatistics(
    val statistics: List<DailySessionStatistic>,
) {
    fun update(deviceUsage: DeviceUsage): SessionStatistics {
        val updatedMap = statistics.associateBy { it.date }.toMutableMap()

        for (dailyUsage in deviceUsage.splitByDate()) {
            val date = dailyUsage.start.toLocalDate()
            val dailySessionStatistic =
                updatedMap[date]
                    ?: DailySessionStatistic(
                        date = date,
                        deviceProfileId = dailyUsage.deviceProfileId,
                    )
            updatedMap[date] = dailySessionStatistic.add(dailyUsage)
        }

        return SessionStatistics(updatedMap.values.sortedBy { it.date })
    }
}
