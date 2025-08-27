package com.yapp.brake.common.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String,
) {
    // COMMON
    BAD_REQUEST(400, "C-400", "잘못된 요청입니다. 필수 값이 누락되었거나 형식이 맞지 않습니다."),
    UNAUTHORIZED(401, "C-401", "인증되지 않은 사용자입니다."),
    FORBIDDEN(403, "C-403", "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C-500", "예기치 못한 서버 오류가 발생했습니다."),

    // JWT
    TOKEN_EXPIRED(401, "T-001", "토큰이 만료되었습니다."),
    TOKEN_INVALID(401, "T-002", "토큰이 유효하지 않습니다."),
    TOKEN_NOT_FOUND(401, "T-003", "헤더에서 토큰을 찾을 수 없습니다."),
    TOKEN_TYPE_MISMATCH(401, "T-004", "토큰의 타입이 올바르지 않습니다."),

    // Member
    MEMBER_NOT_FOUND(404, "M-001", "사용자를 찾을 수 없습니다."),
    MEMBER_INVALID(400, "M-002", "사용자 정보가 유효하지 않습니다."),

    // Group
    GROUP_NOT_FOUND(404, "G-001", "관리 앱 그룹을 찾을 수 없습니다."),

    // Device Profile
    DEVICE_PROFILE_NOT_FOUND(404, "D-001", "디바이스 프로필을 찾을 수 없습니다."),
    DEVICE_PROFILE_INVALID(400, "D-002", "디바이스 프로필 정보가 유효하지 않습니다."),

    // OAuth
    OAUTH_APPLE_AUTH_INVALID(400, "O-001", "유효하지 않은 애플 인증 정보입니다."),
    OAUTH_APPLE_API_SERVER_ERROR(500, "O-002", "애플 로그인 처리 중 내부 오류가 발생했습니다."),
    OAUTH_KAKAO_AUTH_INVALID(400, "O-003", "유효하지 않은 카카오 인증 정보입니다."),
    OAUTH_KAKAO_API_SERVER_ERROR(500, "O-004", "카카오 로그인 처리 중 내부 오류가 발생했습니다."),
}
