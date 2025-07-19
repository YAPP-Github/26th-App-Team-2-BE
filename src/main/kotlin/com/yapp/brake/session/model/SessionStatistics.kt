package com.yapp.brake.session.model

data class SessionStatistics(
    val statistics: List<DailySessionStatistics>,
) {
    fun update(session: Session): SessionStatistics {
        val updatedMap = statistics.associateBy { it.date }.toMutableMap()

        for (split in session.splitByDate()) {
            val date = split.start.toLocalDate()
            val existing =
                updatedMap[date] ?: DailySessionStatistics(
                    date = date,
                    memberId = split.memberId,
                )
            updatedMap[date] = existing.add(split)
        }

        return SessionStatistics(updatedMap.values.sortedBy { it.date })
    }
}
