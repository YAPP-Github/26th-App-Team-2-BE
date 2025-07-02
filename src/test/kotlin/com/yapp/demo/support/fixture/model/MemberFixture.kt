package com.yapp.demo.support.fixture.model

import com.yapp.demo.common.enums.Role
import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.member.model.Member
import com.yapp.demo.member.model.MemberState
import java.time.LocalDateTime
import java.util.UUID

fun memberFixture(
    id: Long = 0L,
    nickname: String? = null,
    authEmail: String = "brake@kakao.com",
    deviceId: String = UUID.randomUUID().toString(),
    socialProvider: SocialProvider = SocialProvider.KAKAO,
    role: Role = Role.USER,
    state: MemberState = MemberState.ACTIVE,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
) = Member(
    id = id,
    nickname = nickname,
    authEmail = authEmail,
    deviceId = deviceId,
    socialProvider = socialProvider,
    role = role,
    state = state,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
