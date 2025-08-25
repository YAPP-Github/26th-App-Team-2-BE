package com.yapp.brake.auth.infrastructure

import java.time.Duration

interface RefreshTokenRepository {
    fun add(
        deviceProfileId: Long,
        token: String,
        ttl: Duration,
    )

    fun read(deviceProfileId: Long): String?

    fun get(deviceProfileId: Long): String

    fun remove(deviceProfileId: Long)
}
