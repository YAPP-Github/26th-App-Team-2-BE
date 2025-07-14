package com.yapp.brake.member.dto.request

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class UpdateNicknameRequestTest {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Nested
    inner class UpdateNicknameRequestBoundaryTest {
        @Test
        fun `닉네임의 길이가 2 미만이면 예외가 발생한다`() {
            val request = UpdateNicknameRequest("A")
            val violations = validator.validate(request)

            assertThat(violations).anyMatch { it.propertyPath.toString() == "nickname" }
        }

        @Test
        fun `닉네임의 길이가 10 초과면 예외가 발생한다`() {
            val request = UpdateNicknameRequest("ABCDEFGHIJK")
            val violations = validator.validate(request)

            assertThat(violations).anyMatch { it.propertyPath.toString() == "nickname" }
        }
    }

    fun `닉네임은 공백이면 예외가 발생한다`() {
        val request = UpdateNicknameRequest("  ")
        val violations = validator.validate(request)

        assertThat(violations).anyMatch { it.propertyPath.toString() == "nickname" }
    }
}
