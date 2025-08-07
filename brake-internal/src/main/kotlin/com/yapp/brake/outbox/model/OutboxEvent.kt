package com.yapp.brake.outbox.model

data class OutboxEvent(
    val outbox: Outbox,
) {
    companion object {
        fun of(outbox: Outbox) = OutboxEvent(outbox)
    }
}
