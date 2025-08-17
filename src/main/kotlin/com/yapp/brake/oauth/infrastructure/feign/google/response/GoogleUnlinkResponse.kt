package com.yapp.brake.oauth.infrastructure.feign.google.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleUnlinkResponse(
    @JsonProperty("id")
    val id: Long,
)
