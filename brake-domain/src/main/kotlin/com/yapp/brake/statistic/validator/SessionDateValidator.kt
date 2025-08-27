package com.yapp.brake.statistic.validator

import com.yapp.brake.statistic.dto.request.QueryStatisticRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class SessionDateValidator : ConstraintValidator<ValidSessionDate, QueryStatisticRequest> {
    override fun isValid(
        value: QueryStatisticRequest,
        context: ConstraintValidatorContext,
    ): Boolean {
        return value.getEndOrDefault() == value.getStartOrDefault() ||
            value.getEndOrDefault().isAfter(value.getStartOrDefault())
    }
}
