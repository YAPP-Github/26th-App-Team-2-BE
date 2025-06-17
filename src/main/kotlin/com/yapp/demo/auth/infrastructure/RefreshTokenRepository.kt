package com.yapp.demo.auth.infrastructure

import java.time.Duration

interface RefreshTokenRepository {
    fun add(
        userId: Long,
        token: String,
        ttl: Duration,
    )

    fun read(userId: Long): String?

    fun remove(userId: Long)
}
