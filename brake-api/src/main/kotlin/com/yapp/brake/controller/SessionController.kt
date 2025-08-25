package com.yapp.brake.controller

import com.yapp.brake.dto.ApiResponse
import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.service.SessionUseCase
import com.yapp.brake.util.getDeviceProfileId
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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
        val response = sessionUseCase.add(getDeviceProfileId(), request)

        return ApiResponse.success(
            status = HttpStatus.CREATED.value(),
            data = response,
        )
    }
}
