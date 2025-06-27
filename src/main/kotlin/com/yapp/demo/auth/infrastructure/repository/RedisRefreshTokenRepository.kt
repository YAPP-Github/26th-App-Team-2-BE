package com.yapp.demo.auth.infrastructure.repository

import com.yapp.demo.auth.infrastructure.RefreshTokenRepository
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
        memberId: Long,
        token: String,
        ttl: Duration,
    ) {
        try {
            redisTemplate.opsForValue().set(generateKey(memberId), token, ttl)
        } catch (e: Exception) {
            logger.error(e) { "[RedisRefreshTokenRepository.add] memberId=$memberId, token=$token" }
            throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(memberId: Long): String? = redisTemplate.opsForValue().get(generateKey(memberId))

    override fun get(memberId: Long): String =
        redisTemplate.opsForValue()
            .get(generateKey(memberId))
            ?: throw CustomException(ErrorCode.TOKEN_NOT_FOUND)

    override fun remove(memberId: Long) {
        redisTemplate.delete(generateKey(memberId))
    }

    private fun generateKey(memberId: Long): String = KEY_FORMAT.format(memberId)

    companion object {
        // auth::refresh-token::{memberId}
        private const val KEY_FORMAT = "auth::refresh-token::%s"
    }
}
