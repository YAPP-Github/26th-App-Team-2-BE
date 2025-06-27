package com.yapp.demo.oauth.infrastructure.feign.apple

import feign.Logger
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean

class AppleFeignConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder = AppleFeignErrorDecoder()

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.FULL
}
