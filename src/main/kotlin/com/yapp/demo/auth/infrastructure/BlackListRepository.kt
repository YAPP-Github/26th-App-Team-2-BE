package com.yapp.demo.auth.infrastructure

import java.time.Duration

interface BlackListRepository {
    fun add(
        token: String,
        ttl: Duration,
    )

    fun read(token: String): String?
}
