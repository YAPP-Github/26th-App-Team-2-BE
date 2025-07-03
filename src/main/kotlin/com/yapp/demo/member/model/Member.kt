package com.yapp.demo.member.model

import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.oauth.model.OAuthUserInfo
import java.time.LocalDateTime

data class Member(
    val id: Long = 0L,
    var nickname: String? = null,
    val oAuthUserInfo: OAuthUserInfo,
    val deviceId: String,
    val socialProvider: SocialProvider,
    val role: Role,
    var state: MemberState,
    val createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {
    fun update(
        nickname: String? = this.nickname,
        status: MemberState = this.state,
    ) = copy(
        nickname = nickname,
        state = status,
    )

    companion object {
        fun create(
            deviceId: String,
            oAuthUserInfo: OAuthUserInfo,
            socialProvider: SocialProvider,
            role: Role,
        ) = Member(
            deviceId = deviceId,
            oAuthUserInfo = oAuthUserInfo,
            socialProvider = socialProvider,
            role = role,
            state = MemberState.HOLD,
        )
    }
}
