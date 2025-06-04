package com.yapp.demo.common.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String,
) {
    BAD_REQUEST(400, "C-400", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "C-500", "예기치 못한 서버 오류가 발생했습니다."),
}
