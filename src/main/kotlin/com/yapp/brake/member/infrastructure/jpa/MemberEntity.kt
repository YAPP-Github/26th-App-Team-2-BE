package com.yapp.brake.member.infrastructure.jpa

import com.yapp.brake.common.enums.Role
import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.common.persistence.Auditable
import com.yapp.brake.member.model.Member
import com.yapp.brake.member.model.MemberState
import com.yapp.brake.oauth.model.OAuthUserInfo
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "member")
class MemberEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId: Long = 0L,
    val deviceId: String,
    val credential: String,
    val authEmail: String,
    @Enumerated(EnumType.STRING)
    val socialProvider: SocialProvider,
    @Enumerated(EnumType.STRING)
    val role: Role,
    state: MemberState,
    nickname: String? = null,
    override var createdAt: LocalDateTime? = null,
    override var updatedAt: LocalDateTime? = null,
) : Auditable() {
    var nickname: String? = nickname
        protected set

    @Enumerated(EnumType.STRING)
    var state: MemberState = state
        protected set

    fun update(entity: MemberEntity) {
        nickname = entity.nickname
        state = entity.state
    }

    fun toDomain() =
        Member(
            id = memberId,
            deviceId = deviceId,
            oAuthUserInfo = OAuthUserInfo(socialProvider, credential, authEmail),
            nickname = nickname,
            role = role,
            state = state,
            createdAt = requireNotNull(createdAt),
            updatedAt = requireNotNull(updatedAt),
        )

    companion object {
        fun create(member: Member) =
            MemberEntity(
                memberId = member.id,
                deviceId = member.deviceId,
                credential = member.oAuthUserInfo.credential,
                authEmail = member.oAuthUserInfo.email,
                nickname = member.nickname,
                socialProvider = member.oAuthUserInfo.socialProvider,
                role = member.role,
                state = member.state,
                createdAt = member.createdAt,
                updatedAt = member.updatedAt,
            )
    }
}
