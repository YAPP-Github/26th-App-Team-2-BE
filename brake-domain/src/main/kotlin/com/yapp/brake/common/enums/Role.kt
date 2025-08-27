package com.yapp.brake.common.enums

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode.BAD_REQUEST

enum class Role(
    val type: String,
) {
    USER("ROLE_USER"),
    ;

    companion object {
        fun from(value: String) =
            entries.find { it.name.uppercase() == value }
                ?: throw CustomException(BAD_REQUEST)
    }
}
