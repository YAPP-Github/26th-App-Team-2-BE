package com.yapp.brake.statistic.model

data class SessionStatistics(
    val statistics: List<DailySessionStatistic>,
) {
    fun update(memberUsage: MemberUsage): SessionStatistics {
        val updatedMap = statistics.associateBy { it.date }.toMutableMap()

        for (dailyUsage in memberUsage.splitByDate()) {
            val date = dailyUsage.start.toLocalDate()
            val dailySessionStatistic =
                updatedMap[date]
                    ?: DailySessionStatistic(
                        date = date,
                        memberId = dailyUsage.memberId,
                    )
            updatedMap[date] = dailySessionStatistic.add(dailyUsage)
        }

        return SessionStatistics(updatedMap.values.sortedBy { it.date })
    }
}
