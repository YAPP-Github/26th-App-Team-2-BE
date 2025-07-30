package com.yapp.brake.outbox.service.listener

import com.yapp.brake.common.event.payload.MemberDeletedEventPayload
import com.yapp.brake.outbox.infrastructure.OutboxReader
import com.yapp.brake.outbox.infrastructure.OutboxWriter
import com.yapp.brake.support.fixture.event.FakeEventHandler
import com.yapp.brake.support.fixture.event.outboxEventPayloadFixture
import com.yapp.brake.support.fixture.model.outboxFixture
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.spy
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MessageRelayTest {
    private val outboxReader = mock<OutboxReader>()
    private val outboxWriter = mock<OutboxWriter>()
    private val eventHandler = spy(FakeEventHandler())
    val messageRelay = MessageRelay(outboxReader, outboxWriter, listOf(eventHandler))

    @Test
    fun `아웃박스를 저장한다`() {
        val outboxEvent = outboxEventPayloadFixture()

        messageRelay.createOutbox(outboxEvent)

        verify(outboxWriter).save(outboxEvent.outbox)
    }

    @Nested
    inner class PublishEventTest {
        @Test
        fun `이벤트를 발행하고 성공적으로 처리되면 아웃박스를 삭제한다`() {
            val outbox = outboxFixture()
            val outboxEvent = outboxEventPayloadFixture(outbox)

            doNothing().whenever(eventHandler).handle(any<MemberDeletedEventPayload>())

            messageRelay.publishEvent(outboxEvent)

            verify(eventHandler).handle(any<MemberDeletedEventPayload>())
            verify(outboxWriter).delete(outbox)
        }

        @Test
        fun `발행한 이벤트가 예외를 던지면 아웃박스를 삭제하지 않는다`() {
            val outbox = outboxFixture()
            val outboxEvent = outboxEventPayloadFixture(outbox)

            doThrow(RuntimeException()).whenever(eventHandler).handle(any())

            messageRelay.publishEvent(outboxEvent)

            verify(outboxWriter, never()).delete(outbox)
        }
    }
}
