package com.yapp.brake.oauth.apple.client

import feign.Logger
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean

class AppleFeignConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder = com.yapp.brake.oauth.apple.client.AppleFeignErrorDecoder()

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.FULL
}
