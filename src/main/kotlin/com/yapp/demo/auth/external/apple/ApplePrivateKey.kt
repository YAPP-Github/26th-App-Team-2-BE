package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.utils.parseECPrivateKey
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import org.springframework.stereotype.Component
import java.security.PrivateKey

@Component
class ApplePrivateKey(private val appleProperties: AppleProperties) {
    val privateKey: PrivateKey = loadPrivateKey()

    private fun loadPrivateKey(): PrivateKey {
        val rawKey =
            javaClass.getResource(appleProperties.headerField)
                ?.readText()
                ?: throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)

        val keyContent =
            rawKey.lines()
                .filterNot { it.contains("PRIVATE KEY") }
                .joinToString("")

        return parseECPrivateKey(keyContent)
    }
}
