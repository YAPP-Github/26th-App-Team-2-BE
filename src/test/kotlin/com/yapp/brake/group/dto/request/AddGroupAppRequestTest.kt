package com.yapp.brake.group.dto.request

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddGroupAppRequestTest {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `관리 앱 이름(식별자)는 공백이면 예외가 발생한다`() {
        val request = AddGroupAppRequest("   ")
        val violations = validator.validate(request)

        assertThat(violations).anyMatch { it.propertyPath.toString() == "name" }
    }
}
