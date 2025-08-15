package com.yapp.brake.exception

class CustomException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
