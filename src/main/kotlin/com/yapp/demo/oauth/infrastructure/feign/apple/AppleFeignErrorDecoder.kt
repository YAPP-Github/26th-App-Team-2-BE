package com.yapp.demo.oauth.infrastructure.feign.apple

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.oauth.infrastructure.feign.apple.response.AppleErrorResponse
import feign.Response
import feign.codec.ErrorDecoder
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class AppleFeignErrorDecoder : ErrorDecoder {
    private val objectMapper: ObjectMapper = ObjectMapper()

    override fun decode(
        methodKey: String?,
        response: Response?,
    ): Exception =
        when (response?.status()) {
            in 400..499 -> {
                val body = objectMapper.readValue(response?.body()?.asInputStream(), AppleErrorResponse::class.java)
                logger.error { "[AppleFeignErrorDecoder.decode] response=$body" }

                CustomException(ErrorCode.UNAUTHORIZED)
            }

            else -> CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
}
