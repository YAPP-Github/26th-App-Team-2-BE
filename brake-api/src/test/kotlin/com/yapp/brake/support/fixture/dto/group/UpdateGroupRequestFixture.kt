package com.yapp.brake.support.fixture.dto.group

import com.yapp.brake.group.dto.request.UpdateGroupAppRequest
import com.yapp.brake.group.dto.request.UpdateGroupRequest

fun updateGroupRequestFixture(
    name: String = "SNS",
    groupApps: List<UpdateGroupAppRequest> =
        listOf(
            updateGroupAppRequestFixture(1L, "카카오톡"),
            updateGroupAppRequestFixture(2L, "인스타그램"),
        ),
) = UpdateGroupRequest(
    name = name,
    groupApps = groupApps,
)
