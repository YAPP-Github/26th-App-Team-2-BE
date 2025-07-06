package com.yapp.brake.outbox.infrastructure.jpa

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface OutboxRepository : JpaRepository<OutboxEntity, Long> {
    fun findAllByOutboxIdAsc(page: Pageable): List<OutboxEntity>
}
