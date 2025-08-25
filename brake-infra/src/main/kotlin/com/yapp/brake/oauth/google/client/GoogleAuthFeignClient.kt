package com.yapp.brake.oauth.google.client

import com.yapp.brake.oauth.google.client.response.GoogleTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "google-auth-client",
    url = "https://oauth2.googleapis.com",
    configuration = [GoogleFeignConfig::class],
)
interface GoogleAuthFeignClient {
    @PostMapping("/token")
    fun getToken(
        @RequestParam(name = "code") code: String,
        @RequestParam(name = "client_id") clientId: String,
        @RequestParam(name = "client_secret") clientSecret: String,
        @RequestParam(name = "redirect_uri") redirectUri: String,
        @RequestParam(name = "grant_type") grantType: String,
        @RequestBody fakeDto: EmptyRequest = EmptyRequest(),
    ): GoogleTokenResponse

    @PostMapping(
        "/revoke",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
    )
    fun revokeToken(
        @RequestParam("token") token: String,
        @RequestBody fakeDto: EmptyRequest = EmptyRequest(),
    )

    data class EmptyRequest(val empty: String = "")
}
