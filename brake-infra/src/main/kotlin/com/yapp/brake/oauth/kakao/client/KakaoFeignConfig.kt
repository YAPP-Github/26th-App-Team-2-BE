package com.yapp.brake.oauth.kakao.client

import feign.Logger
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean

class KakaoFeignConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder = com.yapp.brake.oauth.kakao.client.KakaoFeignErrorDecoder()

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.FULL
}
