package com.yapp.brake.common.event

import com.yapp.brake.common.event.payload.AuthWithdrawEventPayload
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EventTest {
    @Test
    fun `직렬화, 역직렬화 테스트`() {
        val payload = AuthWithdrawEventPayload(userId = 123456L)
        val event = Event.of(EventType.AUTH_WITHDRAW, payload)
        val json = event.toJson()

        // println("json = $json")

        val result = Event.fromJson(json)

        assertThat(result!!.type).isEqualTo(event.type)
        assertThat(result.payload).isInstanceOf(payload::class.java)

        val resultPayload = result.payload as AuthWithdrawEventPayload

        assertThat(resultPayload.userId).isEqualTo(payload.userId)
    }
}
