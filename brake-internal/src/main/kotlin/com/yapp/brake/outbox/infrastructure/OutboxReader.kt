package com.yapp.brake.outbox.infrastructure

import com.yapp.brake.outbox.model.Outbox

interface OutboxReader {
    fun findAll(pageSize: Int): List<Outbox>
}
