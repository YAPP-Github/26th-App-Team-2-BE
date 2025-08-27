package com.yapp.brake.oauth.apple.client.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AppleErrorResponse(
    @JsonProperty("error")
    val error: String?,
    @JsonProperty("error_description")
    val description: String?,
)
