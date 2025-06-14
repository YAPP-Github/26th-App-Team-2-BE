package com.yapp.demo.auth.service

import com.yapp.demo.common.config.properties.JwtProperties
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

    /**
     * Generates a JWT access token for the specified user ID.
     *
     * The token includes the user ID as the subject and a claim indicating it is an access token.
     * The token's expiration time is determined by the configured access token expiry setting.
     *
     * @param userId The unique identifier of the user for whom the access token is generated.
     * @return A signed JWT access token as a string.
     */
    fun generateAccessToken(userId: String): String {
        return generateToken(
            userId = userId,
            type = TOKEN_TYPE_ACCESS,
            expiryMillis = jwtProperties.accessTokenExpiryTime * 1000L,
        )
    }

    /**
     * Generates a JWT refresh token for the specified user ID.
     *
     * The refresh token includes the user ID as the subject and a claim indicating its type as "refresh".
     * The token's expiration time is determined by the configured refresh token expiry duration.
     *
     * @param userId The unique identifier of the user for whom the refresh token is generated.
     * @return A signed JWT refresh token as a string.
     */
    fun generateRefreshToken(userId: String): String {
        return generateToken(
            userId = userId,
            type = TOKEN_TYPE_REFRESH,
            expiryMillis = jwtProperties.refreshTokenExpiryTime * 1000L,
        )
    }

    /**
     * Extracts the user ID from the subject claim of a JWT token.
     *
     * @param token The JWT token string.
     * @return The user ID as a Long.
     * @throws CustomException if the token is expired or invalid.
     */
    fun extractUserId(token: String): Long {
        val claims = getClaims(token)

        return claims.payload.subject.toLong()
    }

    /**
     * Determines whether the provided JWT token is an access token.
     *
     * @param token The JWT token string to check.
     * @return `true` if the token's type claim is "access"; otherwise, `false`.
     */
    fun isAccessToken(token: String): Boolean {
        val claims = getClaims(token)
        val type = claims.payload[TOKEN_TYPE_KEY] as? String

        return type == TOKEN_TYPE_ACCESS
    }

    /**
     * Determines whether the provided JWT token is a refresh token.
     *
     * @param token The JWT token to check.
     * @return `true` if the token's type claim is "refresh"; otherwise, `false`.
     */
    fun isRefreshToken(token: String): Boolean {
        val claims = getClaims(token)
        val type = claims.payload[TOKEN_TYPE_KEY] as? String

        return type == TOKEN_TYPE_REFRESH
    }

    /**
     * Creates a JWT token for the specified user with a given token type and expiration.
     *
     * The token includes the user ID as the subject, a custom claim indicating the token type,
     * the issued-at timestamp, and the expiration time. The token is signed using the decoded secret key.
     *
     * @param userId The user identifier to set as the token's subject.
     * @param type The type of token (e.g., "access" or "refresh") to include as a claim.
     * @param expiryMillis The validity period of the token in milliseconds from the current time.
     * @return The generated JWT as a compact string.
     */
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
            .signWith(decodedSecretKey)
            .compact()
    }

    /**
         * Parses and validates the given JWT token, returning its claims if valid.
         *
         * @param token The JWT token to parse and validate.
         * @return The parsed claims contained in the token.
         * @throws CustomException If the token is expired or invalid.
         */
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
        private const val TOKEN_TYPE_ACCESS = "access"
        private const val TOKEN_TYPE_REFRESH = "refresh"
    }
}
