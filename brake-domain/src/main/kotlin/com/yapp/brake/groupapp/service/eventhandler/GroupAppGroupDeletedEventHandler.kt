package com.yapp.brake.groupapp.service.eventhandler

import com.yapp.brake.common.event.EventHandler
import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.GroupDeletedEventPayload
import com.yapp.brake.groupapp.infrastructure.GroupAppWriter
import org.springframework.stereotype.Component

@Component
class GroupAppGroupDeletedEventHandler(
    private val groupAppWriter: GroupAppWriter,
) : EventHandler<GroupDeletedEventPayload> {
    override val eventType: EventType
        get() = EventType.GROUP_DELETED

    override fun handle(payload: GroupDeletedEventPayload) {
        groupAppWriter.deleteByGroupId(payload.groupId)
    }
}
