package com.yapp.brake.outbox.infrastructure

import com.yapp.brake.outbox.model.Outbox

interface OutboxWriter {
    fun save(outbox: Outbox)

    fun delete(outbox: Outbox)
}
