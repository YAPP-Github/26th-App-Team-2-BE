package com.yapp.demo.common.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String,
) {
    BAD_REQUEST(400, "C-400", "잘못된 요청입니다."),
    UNAUTHORIZED(401, "C-401", "인증되지 않은 사용자입니다."),
    FORBIDDEN(403, "C-403", "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C-500", "예기치 못한 서버 오류가 발생했습니다."),
}
