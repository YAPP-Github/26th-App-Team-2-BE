package com.yapp.brake.constants

internal const val TOKEN_TYPE_ACCESS = "access"
internal const val TOKEN_TYPE_REFRESH = "refresh"
internal val ALLOWED_URIS =
    listOf(
        "/v1/auth/login",
        "/v1/auth/refresh",
        "/static/**",
        "/v1/docs/**",
        "/actuator",
        "/actuator/**",
        "/static",
    )
