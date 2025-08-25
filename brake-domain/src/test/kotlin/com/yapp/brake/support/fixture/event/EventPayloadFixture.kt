package com.yapp.brake.support.fixture.event

import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.common.event.payload.MemberDeletedEventPayload
import com.yapp.brake.outbox.model.Outbox
import com.yapp.brake.outbox.model.OutboxEvent
import com.yapp.brake.support.fixture.model.outboxFixture

fun outboxEventPayloadFixture(outbox: Outbox = outboxFixture()) =
    OutboxEvent(
        outbox = outbox,
    )

fun memberDeletedEventPayloadFixture(
    memberId: Long = 1234L,
    socialProvider: String = SocialProvider.KAKAO.name,
    authId: String = "1231023",
    authEmail: String = "auth@email.com",
) = MemberDeletedEventPayload(
    memberId = memberId,
    socialProvider = socialProvider,
    authId = authId,
    authEmail = authEmail,
)
