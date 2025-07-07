package com.yapp.brake.common.event

import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.common.event.payload.MemberDeletedEventPayload
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class EventTest {
    @Test
    fun `직렬화, 역직렬화 테스트`() {
        val payload =
            MemberDeletedEventPayload(
                memberId = 12345L,
                socialProvider = SocialProvider.KAKAO.name,
                authId = "123124",
                authEmail = "auth@kakao.com",
                deviceId = UUID.randomUUID().toString(),
            )

        val event = Event.of(EventType.MEMBER_DELETED, payload)
        val json = event.toJson()

        // println("json = $json")

        val result = Event.fromJson(json)

        assertThat(result!!.type).isEqualTo(event.type)
        assertThat(result.payload).isInstanceOf(payload::class.java)

        val resultPayload = result.payload as MemberDeletedEventPayload

        assertThat(resultPayload).isEqualTo(payload)
    }
}
