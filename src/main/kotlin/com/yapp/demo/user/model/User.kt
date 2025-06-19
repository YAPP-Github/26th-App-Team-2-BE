package com.yapp.demo.user.model

import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import java.time.LocalDateTime

data class User(
    val id: Long,
    var nickname: String? = null,
    val authEmail: String,
    val socialType: SocialProvider,
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
            id: Long,
            authEmail: String,
            socialType: SocialProvider,
            role: Role,
        ) = User(
            id = id,
            authEmail = authEmail,
            socialType = socialType,
            role = role,
            status = UserStatus.ACTIVE,
        )
    }
}
