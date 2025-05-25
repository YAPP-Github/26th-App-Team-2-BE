package com.yapp.demo.common.controller

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController(
    private val env: Environment
) {
    @GetMapping("/health")
    fun health(): String = "ok"

}
