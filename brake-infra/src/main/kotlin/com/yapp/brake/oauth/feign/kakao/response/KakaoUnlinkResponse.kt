package com.yapp.brake.oauth.feign.kakao.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUnlinkResponse(
    @JsonProperty("id")
    val id: Long,
)
