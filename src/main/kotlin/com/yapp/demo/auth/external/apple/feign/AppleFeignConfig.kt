package com.yapp.demo.auth.external.apple.feign

import feign.Logger
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean

class AppleFeignConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder = AppleFeignErrorDecoder()

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.FULL
}
