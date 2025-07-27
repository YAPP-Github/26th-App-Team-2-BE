package com.yapp.brake.common.controller

import com.yapp.brake.common.exception.ErrorCode
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/docs")
class DocsController {
    @GetMapping("/swagger")
    fun redirectToSwaggerUi(): String {
        return "redirect:/static/swagger/swagger-ui.html"
    }

    @GetMapping("/error-codes")
    fun errorCodes(model: Model): String {
        model.addAttribute("errorCodes", ErrorCode.entries)
        return "error-codes"
    }
}
