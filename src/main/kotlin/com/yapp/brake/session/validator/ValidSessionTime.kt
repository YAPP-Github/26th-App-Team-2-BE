package com.yapp.brake.session.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SessionTimeValidator::class])
annotation class ValidSessionTime(
    val message: String = "종료 시각은 시작 시각보다 이후여야 합니다.",
    val maxDays: Long = 1,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
