package com.yapp.demo.common.security

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import org.springframework.security.core.context.SecurityContextHolder

fun getUserId(): Long =
    SecurityContextHolder.getContext().authentication
        ?.name
        ?.toLongOrNull()
        ?: throw CustomException(ErrorCode.USER_INVALID)
