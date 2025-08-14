package com.yapp.brake.auth.infrastructure

interface TokenProvider {
    fun generateAccessToken(
        memberId: Long,
        deviceProfileId: Long,
    ): String

    fun generateRefreshToken(
        memberId: Long,
        deviceProfileId: Long,
    ): String

    fun extractMemberId(
        token: String,
        tokenType: String,
    ): Long

    fun extractProfileId(token: String): Long

    fun extractExpiration(token: String): Long
}
