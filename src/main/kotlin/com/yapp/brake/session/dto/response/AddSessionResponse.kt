package com.yapp.brake.session.dto.response

data class AddSessionResponse(
    val sessionId: Long,
) {
    companion object {
        fun from(sessionId: Long) = AddSessionResponse(sessionId)
    }
}
