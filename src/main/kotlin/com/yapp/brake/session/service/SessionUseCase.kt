package com.yapp.brake.session.service

import com.yapp.brake.session.dto.request.AddSessionRequest
import com.yapp.brake.session.dto.response.AddSessionResponse
import com.yapp.brake.session.dto.response.SessionStatisticsResponse
import java.time.LocalDate

interface SessionUseCase {
    fun add(
        memberId: Long,
        request: AddSessionRequest,
    ): AddSessionResponse

    fun get(
        memberId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): SessionStatisticsResponse
}
