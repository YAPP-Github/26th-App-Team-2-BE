package com.yapp.brake.support.fixture.dto.group

import com.yapp.brake.group.dto.request.AddGroupAppRequest
import com.yapp.brake.group.dto.request.CreateGroupIosRequest
import com.yapp.brake.group.dto.request.CreateGroupRequest
import com.yapp.brake.support.restdocs.toJsonString

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

fun createGroupIosRequestFixture(
    name: String = "SNS",
    groupApps: List<AddGroupAppRequest> =
        listOf(
            addGroupAppRequestFixture("카카오톡"),
            addGroupAppRequestFixture("인스타그램"),
        ),
) = CreateGroupIosRequest(
    name = name,
    groupApps = groupApps.toJsonString(),
)
