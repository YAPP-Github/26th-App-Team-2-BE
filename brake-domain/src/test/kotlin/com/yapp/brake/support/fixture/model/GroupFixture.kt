package com.yapp.brake.support.fixture.model

import com.yapp.brake.group.model.Group
import java.time.LocalDateTime

fun groupFixture(
    groupId: Long = 0L,
    deviceProfileId: Long = 1234L,
    name: String = "SNS",
    createdAt: LocalDateTime? = null,
    updatedAt: LocalDateTime? = null,
) = Group(
    groupId = groupId,
    deviceProfileId = deviceProfileId,
    name = name,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
