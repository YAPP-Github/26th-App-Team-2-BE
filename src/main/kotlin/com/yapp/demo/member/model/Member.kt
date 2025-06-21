package com.yapp.demo.member.model

import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import java.time.LocalDateTime

data class Member(
    val id: Long = 0L,
    var nickname: String? = null,
    val authEmail: String,
    val socialProvider: SocialProvider,
    val role: Role,
    var status: MemberStatus,
    val createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
) {
    fun update(
        nickname: String? = this.nickname,
        status: MemberStatus = this.status,
    ) = copy(
        nickname = nickname,
        status = status,
    )

    companion object {
        fun create(
            authEmail: String,
            socialProvider: SocialProvider,
            role: Role,
        ) = Member(
            authEmail = authEmail,
            socialProvider = socialProvider,
            role = role,
            status = MemberStatus.ACTIVE,
        )
    }
}
