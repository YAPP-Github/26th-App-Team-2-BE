package com.yapp.demo.auth.infrastructure

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

private val logger = KotlinLogging.logger {}

@Repository
class RedisRefreshTokenRepository(
    private val redisTemplate: StringRedisTemplate,
) : RefreshTokenRepository {
    override fun add(
        userId: Long,
        token: String,
        ttl: Duration,
    ) {
        try {
            redisTemplate.opsForValue().set(generateKey(userId), token, ttl)
        } catch (e: Exception) {
            logger.error(e) { "[RedisRefreshTokenRepository.add] userId=$userId, token=$token" }
            throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(userId: Long): String? = redisTemplate.opsForValue().get(generateKey(userId))

    override fun remove(userId: Long) {
        redisTemplate.delete(generateKey(userId))
    }

    private fun generateKey(userId: Long): String = KEY_FORMAT.format(userId)

    companion object {
        // auth::refresh-token::{userid}
        private const val KEY_FORMAT = "auth::refresh-token::%s"
    }
}
