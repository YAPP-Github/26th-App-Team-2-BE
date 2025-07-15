package com.yapp.brake.session.validator

import com.yapp.brake.session.dto.request.AddSessionRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class SessionTimeValidator : ConstraintValidator<ValidSessionTime, AddSessionRequest> {
    override fun isValid(
        value: AddSessionRequest,
        context: ConstraintValidatorContext,
    ): Boolean {
        return value.end.isAfter(value.start)
    }
}
