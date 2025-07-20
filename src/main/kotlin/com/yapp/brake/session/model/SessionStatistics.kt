package com.yapp.brake.session.model

data class SessionStatistics(
    val statistics: List<DailySessionStatistic>,
) {
    fun update(session: Session): SessionStatistics {
        val updatedMap = statistics.associateBy { it.date }.toMutableMap()

        for (dailySession in session.splitByDate()) {
            val date = dailySession.start.toLocalDate()
            val dailySessionStatistic =
                updatedMap[date]
                    ?: DailySessionStatistic(
                        date = date,
                        memberId = dailySession.memberId,
                    )
            updatedMap[date] = dailySessionStatistic.add(dailySession)
        }

        return SessionStatistics(updatedMap.values.sortedBy { it.date })
    }
}
