package com.yapp.brake.outbox.jpa

import com.yapp.brake.outbox.model.Outbox
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class OutboxJpaReader(
    private val outboxRepository: OutboxRepository,
) : OutboxReader {
    override fun findAll(pageSize: Int): List<Outbox> {
        val pageable = Pageable.ofSize(pageSize)
        return outboxRepository.findAllByOrderByCreatedAtAsc(pageable).map { it.toDomain() }
    }
}
