package com.yapp.brake.statistic.service.eventhandler

import com.yapp.brake.common.event.EventHandler
import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.SessionAddedEventPayload
import com.yapp.brake.session.utils.generateBetweenDates
import com.yapp.brake.statistic.infrastructure.DailySessionStatisticReader
import com.yapp.brake.statistic.infrastructure.DailySessionStatisticWriter
import com.yapp.brake.statistic.model.MemberUsage
import org.springframework.stereotype.Component

@Component
class StatisticsUpdatedEventHandler(
    private val dailySessionStatisticReader: DailySessionStatisticReader,
    private val dailySessionStatisticWriter: DailySessionStatisticWriter,
) : EventHandler<SessionAddedEventPayload> {
    override val eventType: EventType = EventType.SESSION_ADDED

    override fun handle(payload: SessionAddedEventPayload) {
        val memberUsage =
            MemberUsage(
                memberId = payload.memberId,
                start = payload.start,
                end = payload.end,
                goalMinutes = payload.goalMinutes,
            )
        val betweenDates = generateBetweenDates(memberUsage.start, memberUsage.end)
        val statistics = dailySessionStatisticReader.getAllByIds(memberUsage.memberId, betweenDates)
        val updated = statistics.update(memberUsage)
        dailySessionStatisticWriter.saveAll(updated)
    }
}
