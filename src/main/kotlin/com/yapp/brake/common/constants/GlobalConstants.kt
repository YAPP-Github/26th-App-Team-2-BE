package com.yapp.brake.common.constants

const val TOKEN_TYPE_ACCESS = "access"
const val TOKEN_TYPE_REFRESH = "refresh"
val ALLOWED_URIS =
    listOf(
        "/v1/auth/login",
        "/v1/auth/refresh",
        "/static/**",
        "/v1/swagger",
        "/actuator",
        "/actuator/**",
        "/static",
    )
