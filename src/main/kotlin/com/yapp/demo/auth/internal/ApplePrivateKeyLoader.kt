package com.yapp.demo.auth.internal

import com.yapp.demo.auth.utils.parseECPrivateKey
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.security.PrivateKey

@Component
class ApplePrivateKeyLoader(
    private val appleProperties: AppleProperties,
    private val resourceLoader: ResourceLoader,
) {
    val privateKey: PrivateKey = loadPrivateKey()

    private fun loadPrivateKey(): PrivateKey {
        val resource = resourceLoader.getResource(appleProperties.authKeyPath)

        if (!resource.exists()) {
            throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        val rawKey = resource.inputStream.bufferedReader().use { it.readText() }

        val keyContent =
            rawKey.lines()
                .filterNot { it.contains("PRIVATE KEY") }
                .joinToString("")

        return parseECPrivateKey(keyContent)
    }
}
