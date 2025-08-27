package com.yapp.brake.group.dto.request

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UpdateGroupAppRequestTest {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `관리 앱 이름(식별자)는 음수면 예외가 발생한다`() {
        val request = UpdateGroupAppRequest(-1L, "package-name", "kakao")
        val violations = validator.validate(request)

        assertThat(violations).anyMatch { it.propertyPath.toString() == "groupAppId" }
    }
}
