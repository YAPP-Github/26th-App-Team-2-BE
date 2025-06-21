package com.yapp.demo.common.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/swagger")
class SwaggerController {
    @GetMapping
    fun redirectToSwaggerUi(): String {
        return "redirect:/static/swagger/swagger-ui.html"
    }
}
