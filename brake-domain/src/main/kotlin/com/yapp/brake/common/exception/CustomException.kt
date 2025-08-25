package com.yapp.brake.common.exception

class CustomException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
