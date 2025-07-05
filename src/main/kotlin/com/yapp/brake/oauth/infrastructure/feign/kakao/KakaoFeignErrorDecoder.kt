package com.yapp.brake.oauth.infrastructure.feign.kakao

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.oauth.infrastructure.feign.kakao.response.KakaoErrorResponse
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
                val body =
                    objectMapper.readValue(
                        response?.body()?.asInputStream(),
                        KakaoErrorResponse::class.java,
                    )
                logger.error { "[KakaoFeignErrorDecoder.decode] response=$body" }

                CustomException(ErrorCode.UNAUTHORIZED)
            }

            else -> CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
}
