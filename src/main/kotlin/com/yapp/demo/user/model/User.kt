package com.yapp.demo.user.model

import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import java.time.LocalDateTime

data class User(
    val id: Long = 0L,
    var nickname: String? = null,
    val authEmail: String,
    val socialProvider: SocialProvider,
    val role: Role,
    var status: UserStatus,
    val createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
) {
    fun update(
        nickname: String? = this.nickname,
        status: UserStatus = this.status,
    ) = copy(
        nickname = nickname,
        status = status,
    )

    companion object {
        fun create(
            authEmail: String,
            socialProvider: SocialProvider,
            role: Role,
        ) = User(
            authEmail = authEmail,
            socialProvider = socialProvider,
            role = role,
            status = UserStatus.ACTIVE,
        )
    }
}
