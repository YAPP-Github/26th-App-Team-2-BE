package com.yapp.demo.auth.external

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class OAuthErrorDecoder : ErrorDecoder {
    override fun decode(
        methodKey: String?,
        response: Response?,
    ): Exception {
        return when (response?.status()) {
            401 -> CustomException(ErrorCode.UNAUTHORIZED)
            403 -> CustomException(ErrorCode.FORBIDDEN)
            in 400..499 -> CustomException(ErrorCode.UNAUTHORIZED)
            else -> CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }
}
