package com.yapp.brake.oauth.infrastructure.feign.kakao

import com.yapp.brake.oauth.infrastructure.feign.kakao.response.KakaoUnlinkResponse
import com.yapp.brake.oauth.infrastructure.feign.kakao.response.KakaoUserInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "kakao-api-client",
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
        headers = ["Content-Type=application/x-www-form-urlencoded;charset=utf-8"],
    )
    fun unlink(
        @RequestHeader("Authorization") adminKey: String,
        @RequestParam("target_id_type") targetIdType: String,
        @RequestParam("target_id") targetId: Long,
    ): KakaoUnlinkResponse
}
