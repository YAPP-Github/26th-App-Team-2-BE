package com.yapp.brake.outbox.service

import com.yapp.brake.common.event.Event
import com.yapp.brake.outbox.infrastructure.OutboxReader
import com.yapp.brake.outbox.infrastructure.OutboxWriter
import com.yapp.brake.outbox.model.Outbox
import com.yapp.brake.outbox.model.OutboxEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger { }

@Component
class MessageRelay(
    private val outboxReader: OutboxReader,
    private val outboxWriter: OutboxWriter,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun createOutbox(outboxEvent: OutboxEvent) {
        logger.info { "[MessageRelay.createOutbox] outboxEvent=$outboxEvent" }
        outboxWriter.save(outboxEvent.outbox)
    }

    @Async("messageRelayPublishEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun publishEvent(outboxEvent: OutboxEvent) {
        publishEvent(outboxEvent.outbox)
    }

    @Scheduled(
        fixedDelay = 10,
        initialDelay = 5,
        timeUnit = TimeUnit.SECONDS,
        scheduler = "messageRelayPublishPendingEventExecutor",
    )
    fun publishPendingEvent() {
        val outboxes = outboxReader.findAll(OUTBOX_RETRY_SIZE)
        outboxes.forEach { publishEvent(it) }
    }

    private fun publishEvent(outbox: Outbox) {
        try {
            val event = Event.fromJson(outbox.payload) ?: return

            applicationEventPublisher.publishEvent(event.payload)

            outboxWriter.delete(outbox)
        } catch (e: Exception) {
            logger.error(e) { "[MessageRelay.publishEvent] outbox=$outbox" }
        }
    }

    companion object {
        private const val OUTBOX_RETRY_SIZE = 100
    }
}
