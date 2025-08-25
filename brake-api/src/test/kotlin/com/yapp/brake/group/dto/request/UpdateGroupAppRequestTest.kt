package com.yapp.brake.group.dto.request

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UpdateGroupAppRequestTest {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Nested
    inner class UpdateGroupAppRequestBoundaryTest {
        @Test
        fun `앱 이름의 길이가 2 미만이면 예외가 발생한다`() {
            val request = UpdateGroupAppRequest(1L, "package-name", "A")
            val violations = validator.validate(request)

            assertThat(violations).anyMatch { it.propertyPath.toString() == "name" }
        }

        @Test
        fun `앱 이름의 길이가 10 초과면 예외가 발생한다`() {
            val request = UpdateGroupAppRequest(1L, "package-name", "ABCDEFGHIJK")
            val violations = validator.validate(request)

            assertThat(violations).anyMatch { it.propertyPath.toString() == "name" }
        }
    }

    @Test
    fun `관리 앱 이름(식별자)는 음수면 예외가 발생한다`() {
        val request = UpdateGroupAppRequest(-1L, "package-name", "kakao")
        val violations = validator.validate(request)

        assertThat(violations).anyMatch { it.propertyPath.toString() == "groupAppId" }
    }
}
