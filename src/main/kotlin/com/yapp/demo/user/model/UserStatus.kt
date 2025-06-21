package com.yapp.demo.user.model

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode.BAD_REQUEST

enum class UserStatus {
    HOLD,
    ACTIVE,
    DELETED,
    ;

    companion object {
        fun from(value: String) =
            entries.find { it.name.uppercase() == value }
                ?: throw CustomException(BAD_REQUEST)
    }
}
