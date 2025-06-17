package com.yapp.demo.auth.external.kakao.feign

import com.yapp.demo.auth.external.kakao.response.KakaoUserInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    value = "kakao-userinfo-client",
    url = "https://kapi.kakao.com",
    configuration = [KakaoFeignConfig::class],
)
interface KakaoUserInfoFeignClient {
    @GetMapping("/v2/user/me")
    fun getUserInfo(
        @RequestHeader("Authorization") authorization: String,
    ): KakaoUserInfoResponse
}
