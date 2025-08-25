package com.yapp.brake.oauth.google.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.oauth.google.client.response.GoogleErrorResponse
import feign.Response
import feign.codec.ErrorDecoder
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class GoogleFeignErrorDecoder : ErrorDecoder {
    private val objectMapper: ObjectMapper = ObjectMapper()

    override fun decode(
        methodKey: String?,
        response: Response?,
    ): Exception =
        when (response?.status()) {
            in 400..499 -> {
                val body =
                    objectMapper.readValue(
                        response?.body()?.asInputStream(),
                        GoogleErrorResponse::class.java,
                    )

                logger.error { "[GoogleFeignErrorDecoder.decode] response=$body" }

                classifyError(body.error)
            }

            else -> CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

    /***
     * @ref: https://developers.google.com/identity/protocols/oauth2/limited-input-device?hl=ko
     */
    private fun classifyError(error: String?): Exception =
        when (error) {
            "invalid_grant" -> CustomException(ErrorCode.UNAUTHORIZED)
            "invalid_client" -> CustomException(ErrorCode.FORBIDDEN)
            "unsupported_grant_type" -> CustomException(ErrorCode.BAD_REQUEST)
            else -> CustomException(ErrorCode.UNAUTHORIZED)
        }
}
