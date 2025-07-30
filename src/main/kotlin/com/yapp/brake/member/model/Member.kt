package com.yapp.brake.member.model

import com.yapp.brake.common.enums.Role
import com.yapp.brake.oauth.model.OAuthUserInfo
import java.time.LocalDateTime

data class Member(
    val id: Long = 0L,
    var nickname: String? = null,
    val oAuthUserInfo: OAuthUserInfo,
    val role: Role,
    var state: MemberState,
    val createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {
    fun update(
        nickname: String? = this.nickname,
        state: MemberState = this.state,
    ) = copy(
        nickname = nickname,
        state = state,
    )

    companion object {
        fun create(
            oAuthUserInfo: OAuthUserInfo,
            role: Role,
        ) = Member(
            oAuthUserInfo = oAuthUserInfo,
            role = role,
            state = MemberState.HOLD,
        )
    }
}
