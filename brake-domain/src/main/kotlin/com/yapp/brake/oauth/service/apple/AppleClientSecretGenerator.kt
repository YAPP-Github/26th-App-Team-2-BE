package com.yapp.brake.oauth.service.apple

import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.Date

@Component
class AppleClientSecretGenerator(
    private val appleProperties: AppleProperties,
    private val applePrivateKeyLoader: ApplePrivateKeyLoader,
) {
    fun getClientSecret(): String {
        val expiration = ZonedDateTime.now().plusMinutes(5)

        return Jwts.builder()
            .header()
            .add(appleProperties.headerField, appleProperties.keyId)
            .and()
            .issuer(appleProperties.teamId)
            .audience().add(appleProperties.audience).and()
            .subject(appleProperties.clientId)
            .expiration(Date.from(expiration.toInstant()))
            .issuedAt(Date())
            .signWith(applePrivateKeyLoader.privateKey, Jwts.SIG.ES256)
            .compact()
    }
}
