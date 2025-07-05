package com.yapp.demo.oauth.infrastructure.feign.kakao.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUnlinkResponse(
    @JsonProperty("id")
    val id: Long,
)
