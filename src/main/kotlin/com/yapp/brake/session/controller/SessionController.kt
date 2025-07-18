package com.yapp.brake.session.controller

import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.common.security.getMemberId
import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.request.QuerySessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.dto.response.SessionStatisticsResponse
import com.yapp.brake.session.service.SessionUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/session")
class SessionController(
    private val sessionUseCase: SessionUseCase,
) {
    @PostMapping
    fun add(
        @Valid @RequestBody
        request: AddSessionRequest,
    ): ApiResponse<AddSessionResponse> {
        val response = sessionUseCase.add(getMemberId(), request)

        return ApiResponse.success(
            code = HttpStatus.CREATED.value(),
            data = response,
        )
    }

    @GetMapping
    fun get(
        @Valid @ModelAttribute
        request: QuerySessionRequest,
    ): ApiResponse<SessionStatisticsResponse> {
        val response = sessionUseCase.get(getMemberId(), request.start, request.end)

        return ApiResponse.success(response)
    }
}
