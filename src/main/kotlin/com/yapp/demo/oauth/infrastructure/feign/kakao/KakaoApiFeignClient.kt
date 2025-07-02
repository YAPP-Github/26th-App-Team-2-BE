package com.yapp.demo.oauth.infrastructure.feign.kakao

import com.yapp.demo.oauth.infrastructure.feign.kakao.response.KakaoUserInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    value = "kakao-userinfo-client",
    url = "https://kapi.kakao.com",
    configuration = [KakaoFeignConfig::class],
)
interface KakaoApiFeignClient {
    @GetMapping(
        "/v2/user/me",
        headers = ["Content-Type=application/x-www-form-urlencoded;charset=utf-8"],
    )
    fun getUserInfo(
        @RequestHeader("Authorization") authorization: String,
    ): KakaoUserInfoResponse

    @PostMapping(
        "/v1/user/unlink",
    )
    fun unlink(
        @RequestHeader("Authorization") accessToken: String,
    ): KakaoUserInfoResponse
}
