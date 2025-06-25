package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.utils.parseECPrivateKey
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.PrivateKey

@Configuration
class AppleKeyConfig(
    @Value("\${oauth.apple.auth-key-path}")
    private val privateKeyPath: String,
) {
    @Bean
    fun applePrivateKey(): PrivateKey {
        val rawKey =
            javaClass.getResource(privateKeyPath)
                ?.readText()
                ?: throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)

        val keyContent =
            rawKey.lines()
                .filterNot { it.contains("PRIVATE KEY") }
                .joinToString("")

        return parseECPrivateKey(keyContent)
    }
}
