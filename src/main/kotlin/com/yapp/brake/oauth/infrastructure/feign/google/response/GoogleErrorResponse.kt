package com.yapp.brake.oauth.infrastructure.feign.google.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GoogleErrorResponse(
    @JsonProperty("error")
    val error: String?,
    @JsonProperty("error_description")
    val description: String?,
)
