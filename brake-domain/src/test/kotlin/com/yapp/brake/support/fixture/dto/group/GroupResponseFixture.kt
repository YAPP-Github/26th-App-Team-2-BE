package com.yapp.brake.support.fixture.dto.group

import com.yapp.brake.group.dto.request.CreateGroupRequest
import com.yapp.brake.group.dto.request.UpdateGroupRequest
import com.yapp.brake.group.dto.response.GroupResponse

fun groupResponseFixture(request: CreateGroupRequest) =
    GroupResponse(
        groupId = 1412L,
        name = request.name,
        groupApps =
            request.groupApps.mapIndexed { idx, ga ->
                groupAppResponseFixture(idx + 1L, ga.name)
            },
    )

fun groupResponseFixture(request: UpdateGroupRequest) =
    GroupResponse(
        groupId = 1412L,
        name = request.name,
        groupApps =
            request.groupApps.mapIndexed { idx, it ->
                groupAppResponseFixture(it.groupAppId ?: (idx + 100L), it.name)
            },
    )
