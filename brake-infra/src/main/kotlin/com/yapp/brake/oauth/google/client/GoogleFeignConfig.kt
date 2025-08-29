package com.yapp.brake.oauth.google.client

import feign.Logger
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean

class GoogleFeignConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder = GoogleFeignErrorDecoder()

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.FULL
}
