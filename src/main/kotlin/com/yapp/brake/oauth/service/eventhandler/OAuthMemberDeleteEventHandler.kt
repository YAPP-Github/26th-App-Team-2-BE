package com.yapp.brake.oauth.service.eventhandler

import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.common.event.EventPayloadHandler
import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.MemberDeletedEventPayload
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.oauth.service.OAuthProvider
import org.springframework.stereotype.Component

@Component
class OAuthMemberDeleteEventHandler(
    private val oauthProviders: List<OAuthProvider>,
) : EventPayloadHandler<MemberDeletedEventPayload> {
    override fun handle(payload: MemberDeletedEventPayload) {
        val authProvider =
            findProvider(SocialProvider.from(payload.socialProvider))
                ?: throw CustomException(ErrorCode.BAD_REQUEST)

        authProvider.withdraw(payload.authId)
    }

    override fun getEventType(): EventType = EventType.MEMBER_DELETED

    private fun findProvider(socialType: SocialProvider): OAuthProvider? =
        oauthProviders.firstOrNull { it.supports(socialType) }
}
