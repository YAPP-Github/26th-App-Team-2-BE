package com.yapp.demo.auth.external.apple.feign.response

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ApplePublicKeyResponseTest {
    private val sampleKey =
        ApplePublicKey(
            kid = "sampleKeyId",
            algorithm = "RS256",
            use = "sig",
            keyType = "RSA",
            modulus = "sampleModulus",
            exponent = "sampleExponent",
        )

    @Test
    fun `kid와 algorithm이 일치하면 참을 반환한다`() {
        val response = ApplePublicKeyResponse(keys = listOf(sampleKey))

        val result = response.getMatchedKey("sampleKeyId")

        assertEquals(result, sampleKey)
    }

    @Test
    fun `kid가 일치하지 않으면 예외가 발생한다`() {
        val response = ApplePublicKeyResponse(keys = listOf(sampleKey))

        val exception =
            assertThrows<CustomException> {
                response.getMatchedKey("wrongKeyId")
            }

        assertEquals(exception.errorCode, ErrorCode.UNAUTHORIZED)
    }

    @Test
    fun `algorithm가 일치하지 않으면 예외가 발생한다`() {
        val nonMatchingKey = sampleKey.copy(algorithm = "HS256")
        val response = ApplePublicKeyResponse(keys = listOf(nonMatchingKey))

        val exception =
            assertThrows<CustomException> {
                response.getMatchedKey("sampleKeyId")
            }

        assertEquals(exception.errorCode, ErrorCode.UNAUTHORIZED)
    }
}
