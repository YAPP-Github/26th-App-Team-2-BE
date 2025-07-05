package com.yapp.brake.support.fixture.model

import com.yapp.brake.common.enums.Role
import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.member.model.Member
import com.yapp.brake.member.model.MemberState
import com.yapp.brake.oauth.model.OAuthUserInfo
import java.time.LocalDateTime
import java.util.UUID

fun memberFixture(
    id: Long = 0L,
    nickname: String? = null,
    oAuthUserInfo: OAuthUserInfo = OAuthUserInfo(SocialProvider.KAKAO, "11111", "brake@kakao.com"),
    deviceId: String = UUID.randomUUID().toString(),
    role: Role = Role.USER,
    state: MemberState = MemberState.ACTIVE,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
) = Member(
    id = id,
    nickname = nickname,
    oAuthUserInfo = oAuthUserInfo,
    deviceId = deviceId,
    role = role,
    state = state,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
