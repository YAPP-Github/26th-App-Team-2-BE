package com.yapp.brake.support.fixture.model

import com.yapp.brake.groupapp.model.GroupApp
import java.time.LocalDateTime

fun groupAppFixture(
    groupAppId: Long = 12345L,
    groupId: Long = 123L,
    appId: String = "kakaoTalk",
    createdAt: LocalDateTime? = null,
) = GroupApp(
    groupAppId = groupAppId,
    groupId = groupId,
    appId = appId,
    createdAt = createdAt,
)
