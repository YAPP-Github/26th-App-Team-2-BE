package com.yapp.brake.oauth.service.listener

import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.common.event.payload.MemberDeletedEventPayload
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.oauth.service.OAuthProvider
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OAuthMemberDeleteEventListener(
    private val oauthProviders: List<OAuthProvider>,
) {
    @EventListener
    fun withdraw(payload: MemberDeletedEventPayload) {
        val authProvider =
            findProvider(SocialProvider.from(payload.socialProvider))
                ?: throw CustomException(ErrorCode.BAD_REQUEST)

        authProvider.withdraw(payload.authId)
    }

    private fun findProvider(socialType: SocialProvider): OAuthProvider? =
        oauthProviders.firstOrNull { it.supports(socialType) }
}
