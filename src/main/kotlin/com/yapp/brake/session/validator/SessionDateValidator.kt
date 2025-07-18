package com.yapp.brake.session.validator

import com.yapp.brake.session.dto.request.QuerySessionRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class SessionDateValidator : ConstraintValidator<ValidSessionDate, QuerySessionRequest> {
    override fun isValid(
        value: QuerySessionRequest,
        context: ConstraintValidatorContext,
    ): Boolean {
        return value.end == value.start || value.end.isAfter(value.start)
    }
}
