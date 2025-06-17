package com.yapp.demo.auth.external.kakao.feign

import com.yapp.demo.auth.external.OAuthErrorDecoder
import feign.Logger
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KakaoFeignConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder = OAuthErrorDecoder()

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.FULL
}
