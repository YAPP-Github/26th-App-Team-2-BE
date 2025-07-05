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

@Table(name = "member")
@Entity
class MemberEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId: Long = 0L,
    val deviceId: String,
    val authId: String,
    val authEmail: String,
    @Enumerated(EnumType.STRING)
    val socialProvider: SocialProvider,
    @Enumerated(EnumType.STRING)
    val role: Role,
    state: MemberState,
    nickname: String? = null,
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
            oAuthUserInfo = OAuthUserInfo(socialProvider, authId, authEmail),
            nickname = nickname,
            role = role,
            state = state,
            createdAt = requireNotNull(createdAt),
            updatedAt = requireNotNull(updatedAt),
        )

    companion object {
        fun from(member: Member) =
            MemberEntity(
                deviceId = member.deviceId,
                authId = member.oAuthUserInfo.id,
                authEmail = member.oAuthUserInfo.email,
                nickname = member.nickname,
                socialProvider = member.oAuthUserInfo.socialProvider,
                role = member.role,
                state = member.state,
            )
    }
}
