package com.yapp.brake.oauth.utils

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.oauth.apple.utils.parseECPrivateKey
import com.yapp.brake.oauth.apple.utils.toRSAPublicKey
import org.junit.jupiter.api.assertThrows
import java.security.KeyPairGenerator
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KeyUtilsKtTest {
    @Test
    fun `정상적으로 PublicKey를 생성한다`() {
        // given
        val keyPairGen = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }
        val publicKey = keyPairGen.genKeyPair().public as RSAPublicKey

        val modulus = Base64.getUrlEncoder().encodeToString(publicKey.modulus.toByteArray())
        val exponent = Base64.getUrlEncoder().encodeToString(publicKey.publicExponent.toByteArray())

        // when
        val result = toRSAPublicKey(modulus, exponent)

        // then
        assertNotNull(result)
        assertTrue(result is RSAPublicKey)
    }

    @Test
    fun `잘못된 인코딩값의 경우 예외가 발생한다`() {
        // given
        val invalidModulus = "!!!!"
        val invalidExponent = "####"

        // then & then
        val exception =
            assertThrows<CustomException> {
                toRSAPublicKey(invalidModulus, invalidExponent)
            }

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.errorCode)
    }

    @Test
    fun `정상적으로 PrivateKey를 생성한다`() {
        // given
        val keyGen = KeyPairGenerator.getInstance("EC").apply { initialize(256) }
        val privateKey = keyGen.genKeyPair().private
        val encodedKey = Base64.getEncoder().encodeToString(privateKey.encoded)

        // when
        val result = parseECPrivateKey(encodedKey)

        // then
        assertNotNull(result)
        assertTrue(result is ECPrivateKey)
    }

    @Test
    fun `잘못된 Base64 입력시 예외가 발생한다`() {
        // given
        val badKey = "invalid-base64"

        // when & then
        val exception =
            assertThrows<CustomException> {
                parseECPrivateKey(badKey)
            }
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.errorCode)
    }
}
