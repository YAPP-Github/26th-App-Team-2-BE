package com.yapp.brake.support.fixture.event

import com.yapp.brake.common.event.EventHandler
import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.MemberDeletedEventPayload

class FakeEventHandler :
    EventHandler<MemberDeletedEventPayload> {
    override val eventType: EventType = EventType.MEMBER_DELETED

    override fun handle(payload: MemberDeletedEventPayload) = Unit
}
