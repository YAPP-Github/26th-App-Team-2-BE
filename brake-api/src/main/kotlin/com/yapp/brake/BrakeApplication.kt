package com.yapp.brake

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [UserDetailsServiceAutoConfiguration::class],
)
class BrakeApplication

fun main(args: Array<String>) {
    runApplication<BrakeApplication>(*args)
}
