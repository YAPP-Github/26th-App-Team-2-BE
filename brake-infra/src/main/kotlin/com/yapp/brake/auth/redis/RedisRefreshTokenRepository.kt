package com.yapp.brake.auth.redis

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
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
        deviceProfileId: Long,
        token: String,
        ttl: Duration,
    ) {
        try {
            redisTemplate.opsForValue().set(generateKey(deviceProfileId), token, ttl)
        } catch (e: Exception) {
            logger.error(e) { "[RedisRefreshTokenRepository.add] deviceProfileId=$deviceProfileId, token=$token" }
            throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(deviceProfileId: Long): String? = redisTemplate.opsForValue().get(generateKey(deviceProfileId))

    override fun get(deviceProfileId: Long): String =
        redisTemplate.opsForValue()
            .get(generateKey(deviceProfileId))
            ?: throw CustomException(ErrorCode.TOKEN_NOT_FOUND)

    override fun remove(deviceProfileId: Long) {
        redisTemplate.delete(generateKey(deviceProfileId))
    }

    private fun generateKey(deviceProfileId: Long): String = KEY_FORMAT.format(deviceProfileId)

    companion object {
        // auth::refresh-token::{deviceProfileId}
        private const val KEY_FORMAT = "auth::refresh-token::%s"
    }
}
