package com.yapp.brake.support.fixture.model

import com.yapp.brake.common.enums.Role
import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.member.model.Member
import com.yapp.brake.member.model.MemberState
import com.yapp.brake.oauth.model.OAuthUserInfo
import java.time.LocalDateTime

fun memberFixture(
    id: Long = 0L,
    nickname: String? = null,
    oAuthUserInfo: OAuthUserInfo = OAuthUserInfo(SocialProvider.KAKAO, "11111", "brake@kakao.com"),
    role: Role = Role.USER,
    state: MemberState = MemberState.ACTIVE,
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
) = Member(
    id = id,
    nickname = nickname,
    oAuthUserInfo = oAuthUserInfo,
    role = role,
    state = state,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
