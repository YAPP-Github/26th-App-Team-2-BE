package com.yapp.demo.member.model

import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import java.time.LocalDateTime

data class Member(
    val id: Long = 0L,
    var nickname: String? = null,
    val deviceId: String,
    val authEmail: String,
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
            authEmail: String,
            socialProvider: SocialProvider,
            role: Role,
        ) = Member(
            deviceId = deviceId,
            authEmail = authEmail,
            socialProvider = socialProvider,
            role = role,
            state = MemberState.HOLD,
        )
    }
}
