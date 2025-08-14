package com.yapp.brake.oauth.apple.client.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * https://developer.apple.com/documentation/signinwithapplerestapi/tokenresponse
 */
data class AppleTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Long,
    @JsonProperty("id_token")
    val idToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("token_type")
    val tokenType: String,
)
