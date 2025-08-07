package com.yapp.brake.support.fixture.dto.group

import com.yapp.brake.group.dto.response.GroupAppResponse

fun groupAppResponseFixture(
    groupAppId: Long = 1L,
    name: String = "카카오톡",
) = GroupAppResponse(
    groupAppId = groupAppId,
    name = name,
)
