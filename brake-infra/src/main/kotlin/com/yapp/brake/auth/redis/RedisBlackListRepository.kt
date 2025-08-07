package com.yapp.brake.auth.redis

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

private val logger = KotlinLogging.logger {}

@Repository
class RedisBlackListRepository(
    private val redisTemplate: StringRedisTemplate,
) : BlackListRepository {
    override fun add(
        token: String,
        ttl: Duration,
    ) {
        try {
            redisTemplate.opsForValue().set(generateKey(token), "", ttl)
        } catch (e: Exception) {
            logger.error(e) { "[RedisBlackListRepository.add] token=$token" }
            throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(token: String): String? = redisTemplate.opsForValue().get(generateKey(token))

    private fun generateKey(accessToken: String): String = KEY_FORMAT.format(accessToken)

    companion object {
        // auth::blacklist::${access-token}
        private const val KEY_FORMAT = "auth::blacklist::%s"
    }
}
