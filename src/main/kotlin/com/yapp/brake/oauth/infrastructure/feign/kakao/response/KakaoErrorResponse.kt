package com.yapp.brake.oauth.infrastructure.feign.kakao.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoErrorResponse(
    @JsonProperty("error")
    val error: String?,
    @JsonProperty("error_description")
    val description: String?,
    @JsonProperty("error_code")
    val code: String?,
)
