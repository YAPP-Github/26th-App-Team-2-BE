package com.yapp.demo.auth.service

import com.yapp.demo.common.config.properties.JwtProperties
import com.yapp.demo.common.constants.TOKEN_TYPE_ACCESS
import com.yapp.demo.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
) {
    private val decodedSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret))

    fun generateAccessToken(userId: String): String {
        return generateToken(
            userId = userId,
            type = TOKEN_TYPE_ACCESS,
            expiryMillis = jwtProperties.accessTokenExpiryTime * 1000L,
        )
    }

    fun generateRefreshToken(userId: String): String {
        return generateToken(
            userId = userId,
            type = TOKEN_TYPE_REFRESH,
            expiryMillis = jwtProperties.refreshTokenExpiryTime * 1000L,
        )
    }

    fun extractUserId(
        token: String,
        tokenType: String,
    ): Long {
        val claims = getClaims(token)
        val type = claims.payload[TOKEN_TYPE_KEY] as? String

        if (type != tokenType) {
            throw CustomException(ErrorCode.TOKEN_TYPE_MISMATCH)
        }

        return claims.payload.subject.toLong()
    }

    private fun generateToken(
        userId: String,
        type: String,
        expiryMillis: Long,
    ): String {
        val now = Date()
        val expirationDate = Date(now.time + expiryMillis)

        return Jwts.builder()
            .subject(userId)
            .claim(TOKEN_TYPE_KEY, type)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(decodedSecretKey, Jwts.SIG.HS256)
            .compact()
    }

    private fun getClaims(token: String): Jws<Claims> =
        try {
            Jwts.parser()
                .verifyWith(decodedSecretKey)
                .build()
                .parseSignedClaims(token)
        } catch (e: ExpiredJwtException) {
            throw CustomException(ErrorCode.TOKEN_EXPIRED)
        } catch (e: JwtException) {
            throw CustomException(ErrorCode.TOKEN_INVALID)
        }

    companion object {
        private const val TOKEN_TYPE_KEY = "token_type"
    }
}
