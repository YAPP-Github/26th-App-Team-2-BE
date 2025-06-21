package com.yapp.demo.member.infrastructure.jpa

import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.common.persistence.Auditable
import com.yapp.demo.member.model.Member
import com.yapp.demo.member.model.MemberStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "member")
@Entity
class MemberEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId: Long = 0L,
    val authEmail: String,
    @Enumerated(EnumType.STRING)
    val socialProvider: SocialProvider,
    @Enumerated(EnumType.STRING)
    val role: Role,
    val deletedAt: LocalDateTime? = null,
    status: MemberStatus,
    nickname: String? = null,
) : Auditable() {
    var nickname: String? = nickname
        protected set

    @Enumerated(EnumType.STRING)
    var status: MemberStatus = status
        protected set

    fun update(entity: MemberEntity) {
        nickname = entity.nickname
        status = entity.status
    }

    fun toDomain() =
        Member(
            id = memberId,
            authEmail = authEmail,
            nickname = nickname,
            socialProvider = socialProvider,
            role = role,
            status = status,
            createdAt = requireNotNull(createdAt),
            updatedAt = requireNotNull(updatedAt),
        )

    companion object {
        fun from(member: Member) =
            MemberEntity(
                authEmail = member.authEmail,
                nickname = member.nickname,
                socialProvider = member.socialProvider,
                role = member.role,
                status = member.status,
            )
    }
}
