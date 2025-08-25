package com.yapp.brake.support.fixture.dto.group

import com.yapp.brake.group.dto.request.AddGroupAppRequest
import com.yapp.brake.group.dto.request.CreateGroupRequest

fun createGroupRequestFixture(
    name: String = "SNS",
    groupApps: List<AddGroupAppRequest> =
        listOf(
            addGroupAppRequestFixture("카카오톡"),
            addGroupAppRequestFixture("인스타그램"),
        ),
) = CreateGroupRequest(
    name = name,
    groupApps = groupApps,
)
