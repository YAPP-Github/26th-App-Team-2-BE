package com.yapp.demo.common.exception

class CustomException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
