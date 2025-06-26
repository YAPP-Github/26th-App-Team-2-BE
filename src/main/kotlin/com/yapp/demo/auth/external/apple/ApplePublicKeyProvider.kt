package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.external.apple.feign.AppleAuthFeignClient
import com.yapp.demo.auth.utils.toRSAPublicKey
import org.springframework.stereotype.Component
import java.security.PublicKey

@Component
class ApplePublicKeyProvider(private val appleAuthFeignClient: AppleAuthFeignClient) {
    fun getPublicKey(kid: String): PublicKey {
        val applePublicKeys = appleAuthFeignClient.getApplePublicKeys()
        val matched = applePublicKeys.getMatchedKey(kid)

        return toRSAPublicKey(matched.modulus, matched.exponent)
    }
}
