package com.yapp.brake.oauth.kakao.client.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUnlinkResponse(
    @JsonProperty("id")
    val id: Long,
)
