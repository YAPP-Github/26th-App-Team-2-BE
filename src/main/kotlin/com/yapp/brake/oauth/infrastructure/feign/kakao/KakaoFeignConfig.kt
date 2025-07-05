package com.yapp.brake.oauth.infrastructure.feign.kakao

import feign.Logger
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean

class KakaoFeignConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder = KakaoFeignErrorDecoder()

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.FULL
}
