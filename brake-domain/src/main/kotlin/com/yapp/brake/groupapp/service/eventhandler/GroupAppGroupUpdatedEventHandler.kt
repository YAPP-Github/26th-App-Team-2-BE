package com.yapp.brake.groupapp.service.eventhandler

import com.yapp.brake.common.event.EventHandler
import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.GroupUpdatedEventPayload
import com.yapp.brake.groupapp.infrastructure.GroupAppWriter
import org.springframework.stereotype.Component

@Component
class GroupAppGroupUpdatedEventHandler(
    private val groupAppWriter: GroupAppWriter,
) : EventHandler<GroupUpdatedEventPayload> {
    override val eventType: EventType
        get() = EventType.GROUP_UPDATED

    override fun handle(payload: GroupUpdatedEventPayload) {
        groupAppWriter.deleteByGroupAppIds(payload.deletedAppIds)
    }
}
