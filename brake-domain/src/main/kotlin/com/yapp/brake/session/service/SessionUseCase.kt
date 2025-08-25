package com.yapp.brake.session.service

import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse

interface SessionUseCase {
    fun add(
        deviceProfileId: Long,
        request: AddSessionRequest,
    ): AddSessionResponse
}
