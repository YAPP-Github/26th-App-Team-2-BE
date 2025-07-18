package com.yapp.brake.session.dto.request

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate
import kotlin.test.Test

class QuerySessionRequestTest {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `마지막날이 첫날보다 이른 경우 유효성 검사에 실패한다`() {
        val invalidRequest =
            QuerySessionRequest(
                start = LocalDate.of(2025, 7, 15),
                end = LocalDate.of(2025, 7, 14),
            )

        val violations = validator.validate(invalidRequest)

        assertEquals(1, violations.size)
        assertNotNull(violations.first { it.propertyPath.toString().isEmpty() })
    }

    @ParameterizedTest
    @ValueSource(ints = [15, 16])
    fun `마지막날이 첫날보다 같거나 늦으면 검증을 통과한다`(day: Int) {
        // given
        val validRequest =
            QuerySessionRequest(
                start = LocalDate.of(2025, 7, 15),
                end = LocalDate.of(2025, 7, day),
            )

        // when
        val violations = validator.validate(validRequest)

        // then
        assertEquals(0, violations.size)
    }
}
