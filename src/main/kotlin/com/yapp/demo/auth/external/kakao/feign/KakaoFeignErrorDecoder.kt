package com.yapp.demo.auth.external.kakao.feign

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.demo.auth.external.kakao.feign.response.KakaoErrorResponse
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import feign.Response
import feign.codec.ErrorDecoder
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class KakaoFeignErrorDecoder : ErrorDecoder {
    val objectMapper: ObjectMapper = ObjectMapper()

    override fun decode(
        methodKey: String?,
        response: Response?,
    ): Exception =
        when (response?.status()) {
            in 400..499 -> {
                val body = objectMapper.readValue(response?.body()?.asInputStream(), KakaoErrorResponse::class.java)
                logger.error { "[KakaoFeignErrorDecoder.decode] response=$body" }

                CustomException(ErrorCode.UNAUTHORIZED)
            }

            else -> CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
}
