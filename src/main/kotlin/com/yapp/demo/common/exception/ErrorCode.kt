package com.yapp.demo.common.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String,
) {
    BAD_REQUEST(400, "C-400", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "C-500", "예기치 못한 서버 오류가 발생했습니다."),

    // JWT
    TOKEN_EXPIRED(400, "T-001", "토큰이 만료되었습니다."),
    TOKEN_INVALID(400, "T-002", "토큰이 유효하지 않습니다."),
    TOKEN_NOT_FOUND(404, "T-003", "토큰을 찾을 수 없습니다."),
    TOKEN_TYPE_MISMATCH(400, "T-004", "토큰의 타입이 올바르지 않습니다."),
}
