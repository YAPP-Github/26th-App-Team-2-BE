package com.yapp.brake.auth.infrastructure

import java.time.Duration

interface RefreshTokenRepository {
    fun add(
        memberId: Long,
        token: String,
        ttl: Duration,
    )

    fun read(memberId: Long): String?

    fun get(memberId: Long): String

    fun remove(memberId: Long)
}
