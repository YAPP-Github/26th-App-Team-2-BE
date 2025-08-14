package com.yapp.brake.oauth.apple.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.oauth.apple.client.response.AppleErrorResponse
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

                classifyError(body.error)
            }

            else -> CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

    /***
     * @ref https://developer.apple.com/documentation/signinwithapplerestapi/errorresponse
     */
    private fun classifyError(error: String?): Exception {
        return when (error) {
            "invalid_grant" -> CustomException(ErrorCode.OAUTH_APPLE_AUTH_INVALID)
            null -> CustomException(ErrorCode.UNAUTHORIZED)
            else -> CustomException(ErrorCode.OAUTH_APPLE_API_SERVER_ERROR)
        }
    }
}
