package com.yapp.brake.support.fixture.model

import com.yapp.brake.groupapp.model.GroupApp
import java.time.LocalDateTime

fun groupAppFixture(
    groupAppId: Long = 12345L,
    groupId: Long = 123L,
    name: String = "kakaoTalk",
    packageName: String = "package-name",
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
) = GroupApp(
    groupAppId = groupAppId,
    groupId = groupId,
    packageName = packageName,
    name = name,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
