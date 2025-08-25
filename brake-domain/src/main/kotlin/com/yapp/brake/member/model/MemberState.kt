package com.yapp.brake.member.model

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode.BAD_REQUEST

enum class MemberState {
    HOLD,
    ACTIVE,
    ;

    companion object {
        fun from(value: String) =
            entries.find { it.name.uppercase() == value }
                ?: throw CustomException(BAD_REQUEST)
    }
}
