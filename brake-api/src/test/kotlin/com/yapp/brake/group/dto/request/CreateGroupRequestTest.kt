package com.yapp.brake.group.dto.request

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CreateGroupRequestTest {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Nested
    inner class CreateGroupRequestBoundaryTest {
        @Test
        fun `닉네임의 길이가 2 미만이면 예외가 발생한다`() {
            val request = CreateGroupRequest("A")
            val violations = validator.validate(request)

            assertThat(violations).anyMatch { it.propertyPath.toString() == "name" }
        }

        @Test
        fun `닉네임의 길이가 10 초과면 예외가 발생한다`() {
            val request = CreateGroupRequest("ABCDEFGHIJK")
            val violations = validator.validate(request)

            assertThat(violations).anyMatch { it.propertyPath.toString() == "name" }
        }
    }
}
