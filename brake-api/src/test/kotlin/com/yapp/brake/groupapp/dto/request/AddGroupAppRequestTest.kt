package com.yapp.brake.groupapp.dto.request

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddGroupAppRequestTest {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `그룹 식별자는 양수가 아니면 예외가 발생한다`() {
        val request = AddGroupAppRequest(-1L, "app-id")
        val violations = validator.validate(request)

        assertThat(violations).anyMatch { it.propertyPath.toString() == "groupId" }
    }

    @Test
    fun `관리 앱 이름(식별자)는 공백이면 예외가 발생한다`() {
        val request = AddGroupAppRequest(123L, "   ")
        val violations = validator.validate(request)

        assertThat(violations).anyMatch { it.propertyPath.toString() == "appId" }
    }
}
