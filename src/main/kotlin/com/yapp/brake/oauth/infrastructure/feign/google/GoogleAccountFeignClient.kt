package com.yapp.brake.oauth.infrastructure.feign.google

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "google-account-client",
    url = "https://accounts.google.com/o/oauth2/revoke",
    configuration = [GoogleFeignConfig::class],
)
interface GoogleAccountFeignClient {
    @PostMapping
    fun revokeToken(
        @RequestParam("token") token: String,
    )
}
