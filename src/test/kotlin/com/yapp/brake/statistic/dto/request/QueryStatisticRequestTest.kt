package com.yapp.brake.statistic.dto.request

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate
import kotlin.test.Test

private const val BETWEEN_DAYS = 6L

class QueryStatisticRequestTest {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `마지막날이 첫날보다 이른 경우 유효성 검사에 실패한다`() {
        // given
        val invalidRequest =
            QueryStatisticRequest(
                start = LocalDate.of(2025, 7, 15),
                end = LocalDate.of(2025, 7, 14),
            )

        // when
        val violations = validator.validate(invalidRequest)

        // then
        assertEquals(1, violations.size)
        assertNotNull(violations.first { it.propertyPath.toString().isEmpty() })
    }

    @ParameterizedTest
    @ValueSource(ints = [15, 16])
    fun `마지막날이 첫날보다 같거나 늦으면 검증을 통과한다`(day: Int) {
        // given
        val validRequest =
            QueryStatisticRequest(
                start = LocalDate.of(2025, 7, 15),
                end = LocalDate.of(2025, 7, day),
            )

        // when
        val violations = validator.validate(validRequest)

        // then
        assertEquals(0, violations.size)
    }

    @Test
    fun `마지막날이 없는 경우 첫날보다 일정 기간 이후 날짜를 반환한다`() {
        // given
        val start = LocalDate.of(2025, 7, 15)
        val withoutEndRequest =
            QueryStatisticRequest(
                start = start,
                end = null,
            )

        // when
        val result = withoutEndRequest.getEndOrDefault()

        // then
        assertEquals(start.plusDays(BETWEEN_DAYS), result)
    }

    @Test
    fun `첫날이 없는 경우 마지막보다 일정 기간 이전 날짜를 반환한다`() {
        // given
        val end = LocalDate.of(2025, 7, 15)
        val withoutStartRequest =
            QueryStatisticRequest(
                start = null,
                end = end,
            )

        // when
        val result = withoutStartRequest.getStartOrDefault()

        // then
        assertEquals(end.minusDays(BETWEEN_DAYS), result)
    }

    @Test
    fun `날짜가 모두 없는 경우 오늘을 기준으로 일정 기간 이전 날짜를 반환한다`() {
        // given
        val today = LocalDate.now()
        val noneDateRequest =
            QueryStatisticRequest(
                start = null,
                end = null,
            )

        // when
        val startResult = noneDateRequest.getStartOrDefault()
        val endResult = noneDateRequest.getEndOrDefault()

        // then
        assertEquals(endResult, today)
        assertEquals(startResult, today.minusDays(BETWEEN_DAYS))
    }
}
