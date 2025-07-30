package com.yapp.brake

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication(
    exclude = [UserDetailsServiceAutoConfiguration::class],
)
@EnableFeignClients
class BrakeApplication

fun main(args: Array<String>) {
    runApplication<BrakeApplication>(*args)
}
