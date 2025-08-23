package com.yapp.brake.oauth.infrastructure.feign.google.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleUserInfoResponse(
    @JsonProperty("email")
    val email: String,
) {
    companion object {
        fun createEmpty() =
            GoogleUserInfoResponse(
                email = "",
            )
    }
}
