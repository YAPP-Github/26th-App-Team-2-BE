package com.yapp.brake.deviceprofile.service.eventhandler

import com.yapp.brake.common.event.EventHandler
import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.MemberDeletedEventPayload
import com.yapp.brake.deviceprofile.infrastructure.DeviceProfileWriter
import org.springframework.stereotype.Component

@Component
class DeviceProfileDeletedEventHandler(
    private val deviceProfileWriter: DeviceProfileWriter,
) : EventHandler<MemberDeletedEventPayload> {
    override val eventType: EventType = EventType.MEMBER_DELETED

    override fun handle(payload: MemberDeletedEventPayload) {
        deviceProfileWriter.deleteAllByMemberId(payload.memberId)
    }
}
