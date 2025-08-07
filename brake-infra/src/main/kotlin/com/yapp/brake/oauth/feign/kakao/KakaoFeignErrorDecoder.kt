package com.yapp.brake.oauth.feign.kakao

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.oauth.feign.kakao.response.KakaoErrorResponse
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

                classifyError(body.error)
            }

            else -> CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

    /***
     * @ref: https://developers.kakao.com/docs/latest/ko/kakaologin/trouble-shooting#token
     * @ref: https://developers.kakao.com/docs/latest/ko/rest-api/reference#error-code-kakaologin
     */
    private fun classifyError(error: String?): Exception =
        error?.let {
            when {
                error.startsWith(KAKAO_ACCESS_TOKEN_ERROR_PREFIX)
                    -> CustomException(ErrorCode.OAUTH_KAKAO_API_SERVER_ERROR)

                error.startsWith(KAKAO_COMMON_ERROR_PREFIX)
                    -> CustomException(ErrorCode.OAUTH_KAKAO_AUTH_INVALID)

                else
                    -> CustomException(ErrorCode.UNAUTHORIZED)
            }
        } ?: CustomException(ErrorCode.UNAUTHORIZED)

    companion object {
        private const val KAKAO_ACCESS_TOKEN_ERROR_PREFIX = "KOE"
        private const val KAKAO_COMMON_ERROR_PREFIX = "-"
    }
}
