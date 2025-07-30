package com.yapp.brake.common.security

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import org.springframework.security.core.context.SecurityContextHolder

fun getMemberId(): Long =
    SecurityContextHolder.getContext().authentication
        ?.name
        ?.toLongOrNull()
        ?: throw CustomException(ErrorCode.MEMBER_INVALID)

fun getDeviceProfileId(): Long =
    SecurityContextHolder.getContext().authentication
        ?.credentials
        ?.toString()
        ?.toLongOrNull()
        ?: throw CustomException(ErrorCode.DEVICE_PROFILE_INVALID)
