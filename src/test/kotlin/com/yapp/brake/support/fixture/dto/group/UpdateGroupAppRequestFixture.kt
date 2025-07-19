package com.yapp.brake.support.fixture.dto.group

import com.yapp.brake.group.dto.request.UpdateGroupAppRequest

fun updateGroupAppRequestFixture(
    groupAppId: Long? = null,
    name: String = "카카오톡",
) = UpdateGroupAppRequest(
    groupAppId = groupAppId,
    name = name,
)
