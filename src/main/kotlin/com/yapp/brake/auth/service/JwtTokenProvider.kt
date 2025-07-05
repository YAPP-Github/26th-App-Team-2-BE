package com.yapp.brake.auth.service

import com.yapp.brake.common.config.properties.JwtProperties
import com.yapp.brake.common.constants.TOKEN_TYPE_ACCESS
import com.yapp.brake.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.member.infrastructure.jpa.MemberJpaReader
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
    private val memberReader: MemberJpaReader,
) {
    private val decodedSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret))

    fun generateAccessToken(memberId: Long): String {
        return generateToken(
            memberId = memberId.toString(),
            type = TOKEN_TYPE_ACCESS,
            expiryMillis = jwtProperties.accessTokenExpiryTime * 1000L,
        )
    }

    fun generateRefreshToken(memberId: Long): String {
        return generateToken(
            memberId = memberId.toString(),
            type = TOKEN_TYPE_REFRESH,
            expiryMillis = jwtProperties.refreshTokenExpiryTime * 1000L,
        )
    }

    fun extractMemberId(
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

    fun extractExpiration(token: String): Long {
        val claims = getClaims(token)
        return claims.payload.expiration.time
    }

    fun getAuthentication(memberId: Long): Authentication {
        val member =
            memberReader.findById(memberId)
                ?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)

        return UsernamePasswordAuthenticationToken(
            member.id,
            null,
            listOf(SimpleGrantedAuthority(member.role.toString())),
        )
    }

    private fun generateToken(
        memberId: String,
        type: String,
        expiryMillis: Long,
    ): String {
        val now = Date()
        val expirationDate = Date(now.time + expiryMillis)

        return Jwts.builder()
            .subject(memberId)
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
