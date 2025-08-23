package com.yapp.brake.oauth.infrastructure.feign.google

import com.yapp.brake.oauth.infrastructure.feign.google.response.GoogleUserInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "google-api-client",
    url = "https://www.googleapis.com",
    configuration = [GoogleFeignConfig::class],
)
interface GoogleApiFeignClient {
    @GetMapping("/oauth2/v3/userinfo")
    fun getUserInfo(
        @RequestHeader("Authorization") token: String,
    ): GoogleUserInfoResponse
}
