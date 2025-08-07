package com.yapp.brake.outbox.infrastructure.jpa

import com.yapp.brake.outbox.model.Outbox
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class OutboxJpaWriter(
    private val outboxRepository: OutboxRepository,
) : OutboxWriter {
    override fun save(outbox: Outbox) {
        val entity = OutboxEntity.from(outbox)
        outboxRepository.save(entity)
    }

    override fun delete(outbox: Outbox) {
        outboxRepository.delete(OutboxEntity.from(outbox))
    }
}
