package com.yapp.demo.user.infrastructure.jpa

import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.common.persistence.Auditable
import com.yapp.demo.user.model.User
import com.yapp.demo.user.model.UserStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "user")
@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0L,
    val authEmail: String,
    @Enumerated(EnumType.STRING)
    val socialProvider: SocialProvider,
    @Enumerated(EnumType.STRING)
    val role: Role,
    val deletedAt: LocalDateTime? = null,
    status: UserStatus,
    nickname: String? = null,
) : Auditable() {
    var nickname: String? = nickname
        protected set

    @Enumerated(EnumType.STRING)
    var status: UserStatus = status
        protected set

    fun update(entity: UserEntity) {
        nickname = entity.nickname
        status = entity.status
    }

    fun toDomain() =
        User(
            id = userId,
            authEmail = authEmail,
            nickname = nickname,
            socialType = socialProvider,
            role = role,
            status = status,
            createdAt = requireNotNull(createdAt),
            updatedAt = requireNotNull(updatedAt),
        )

    companion object {
        fun from(user: User) =
            UserEntity(
                authEmail = user.authEmail,
                nickname = user.nickname,
                socialProvider = user.socialType,
                role = user.role,
                status = user.status,
            )
    }
}
