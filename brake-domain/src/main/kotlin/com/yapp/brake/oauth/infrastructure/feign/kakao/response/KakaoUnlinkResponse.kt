package com.yapp.brake.oauth.infrastructure.feign.kakao.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUnlinkResponse(
    @JsonProperty("id")
    val id: Long,
)
