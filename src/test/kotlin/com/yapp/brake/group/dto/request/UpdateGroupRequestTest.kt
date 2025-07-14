package com.yapp.brake.group.dto.request

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UpdateGroupRequestTest {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Nested
    inner class UpdateGroupRequestBoundaryTest {
        @Test
        fun `닉네임의 길이가 2 미만이면 예외가 발생한다`() {
            val request = UpdateGroupRequest(1L, "A")
            val violations = validator.validate(request)

            assertThat(violations).anyMatch { it.propertyPath.toString() == "name" }
        }

        @Test
        fun `닉네임의 길이가 10 초과면 예외가 발생한다`() {
            val request = UpdateGroupRequest(1L, "ABCDEFGHIJK")
            val violations = validator.validate(request)

            assertThat(violations).anyMatch { it.propertyPath.toString() == "name" }
        }
    }

    @Test
    fun `괸리 앱 그룹 식별자는 양수가 아니면 예외가 발생 한다`() {
        val request1 = UpdateGroupRequest(0L, "ABCDE")
        val request2 = UpdateGroupRequest(-1L, "ABCDE")

        val violations1 = validator.validate(request1)
        val violations2 = validator.validate(request2)

        assertThat(violations1).anyMatch { it.propertyPath.toString() == "groupId" }
        assertThat(violations2).anyMatch { it.propertyPath.toString() == "groupId" }
    }
}
