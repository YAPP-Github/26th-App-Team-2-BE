package com.yapp.brake.statistic.service.eventhandler

import com.yapp.brake.common.event.EventHandler
import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.StatisticsUpdatedEventPayload
import com.yapp.brake.session.model.Session
import com.yapp.brake.session.model.Snooze
import com.yapp.brake.session.utils.generateBetweenDates
import com.yapp.brake.statistic.infrastructure.DailySessionStatisticReader
import com.yapp.brake.statistic.infrastructure.DailySessionStatisticWriter
import org.springframework.stereotype.Component

@Component
class StatisticsUpdatedEventHandler(
    private val dailySessionStatisticReader: DailySessionStatisticReader,
    private val dailySessionStatisticWriter: DailySessionStatisticWriter,
) : EventHandler<StatisticsUpdatedEventPayload> {
    override val eventType: EventType = EventType.STATISTICS_UPDATED

    override fun handle(payload: StatisticsUpdatedEventPayload) {
        val session =
            Session(
                memberId = payload.memberId,
                start = payload.start,
                end = payload.end,
                groupId = payload.groupId,
                goalMinutes = payload.goalMinutes,
                snooze = Snooze.empty(),
            )
        val betweenDates = generateBetweenDates(session.start, session.end)
        val statistics = dailySessionStatisticReader.getAllByIds(session.memberId, betweenDates)
        val updated = statistics.update(session)
        dailySessionStatisticWriter.saveAll(updated)
    }
}
