package com.yapp.brake.session.validator

import com.yapp.brake.session.dto.request.AddSessionRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.temporal.ChronoUnit

class SessionTimeValidator : ConstraintValidator<ValidSessionTime, AddSessionRequest> {
    private var maxDays: Long = 1

    override fun initialize(constraintAnnotation: ValidSessionTime) {
        maxDays = constraintAnnotation.maxDays
    }

    override fun isValid(
        value: AddSessionRequest,
        context: ConstraintValidatorContext,
    ): Boolean {
        return ChronoUnit.DAYS.between(value.start, value.end) <= maxDays && value.end.isAfter(value.start)
    }
}
