package com.yapp.demo.oauth.utils

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.ValueSource
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertEquals

class TokenUtilsKtTest {
    @Test
    fun `정상적인 JWT 헤더를 파싱한다`() {
        // given
        val headerJson =
            """
            {"alg":"RS256","kid":"ABC123"}
            """.trimIndent()
        val encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.toByteArray())
        val token = "$encodedHeader.payload.signature"

        // when
        val result = parseHeaders(token)

        // then
        assertEquals("RS256", result["alg"])
        assertEquals("ABC123", result["kid"])
    }

    @ParameterizedTest
    @ValueSource(strings = ["only.one", "nothing"])
    @EmptySource
    fun `토큰에 점이 2개 미만이면 예외가 발생한다`(invalidToken: String) {
        // when & then
        val exception =
            assertThrows<CustomException> {
                parseHeaders(invalidToken)
            }

        assertEquals(ErrorCode.TOKEN_INVALID, exception.errorCode)
    }

    @Test
    fun `디코딩 실패 시 예외가 발생한다`() {
        // given
        val token = "!!!.payload.signature"

        // when & then
        val exception =
            assertThrows<CustomException> {
                parseHeaders(token)
            }

        assertEquals(ErrorCode.TOKEN_INVALID, exception.errorCode)
    }

    @Test
    fun `파싱 실패 시 예외가 발생한다`() {
        // given
        val invalidJson = """invalid json"""
        val encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(invalidJson.toByteArray())
        val token = "$encoded.payload.signature"

        // when & then
        val exception =
            assertThrows<CustomException> {
                parseHeaders(token)
            }

        assertEquals(ErrorCode.TOKEN_INVALID, exception.errorCode)
    }
}
