package com.yapp.brake

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [UserDetailsServiceAutoConfiguration::class],
    scanBasePackages = [
        "com.yapp.api",
        "com.yapp.infra",
        "com.yapp.domain",
        "com.yapp.internal",
    ],
)
class BrakeApplication

fun main(args: Array<String>) {
    runApplication<BrakeApplication>(*args)
}
