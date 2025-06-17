package com.yapp.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication(
    exclude = [UserDetailsServiceAutoConfiguration::class],
)
@EnableFeignClients
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
