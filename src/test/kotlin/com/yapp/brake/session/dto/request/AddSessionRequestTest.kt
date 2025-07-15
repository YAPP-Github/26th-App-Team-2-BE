package com.yapp.brake.session.dto.request

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDateTime
import kotlin.test.Test

class AddSessionRequestTest {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `음수 값이 있을 경우 유효성 검사에 실패한다`() {
        // given
        val invalidRequest =
            AddSessionRequest(
                groupId = -1L,
                start = LocalDateTime.of(2025, 7, 15, 9, 0),
                end = LocalDateTime.of(2025, 7, 15, 10, 0),
                goalTime = -1L,
                snoozeUnit = -1,
                snoozeCount = -1,
            )

        // when
        val violations = validator.validate(invalidRequest)

        // then
        assertEquals(4, violations.size)
    }

    @Test
    fun `모든 값이 유효하면 검증을 통과한다`() {
        // given
        val validRequest =
            AddSessionRequest(
                groupId = 1L,
                start = LocalDateTime.of(2025, 7, 15, 9, 0),
                end = LocalDateTime.of(2025, 7, 15, 10, 0),
                goalTime = 5L,
                snoozeUnit = 0,
                snoozeCount = 0,
            )

        // when
        val violations = validator.validate(validRequest)

        // then
        assertEquals(0, violations.size)
    }
}
