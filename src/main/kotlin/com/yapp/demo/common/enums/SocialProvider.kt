package com.yapp.demo.common.enums

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode.BAD_REQUEST

enum class SocialProvider {
    KAKAO,
    APPLE,
    ;

    companion object {
        fun from(value: String) =
            entries.find { it.name.lowercase() == value }
                ?: throw throw CustomException(BAD_REQUEST)
    }
}
