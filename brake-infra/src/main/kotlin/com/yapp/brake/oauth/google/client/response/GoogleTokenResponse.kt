package com.yapp.brake.oauth.google.client.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String? = null,
) {
    companion object {
        fun createEmpty() =
            GoogleTokenResponse(
                accessToken = "",
                refreshToken = "",
            )
    }
}
