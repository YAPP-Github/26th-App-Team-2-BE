package com.yapp.brake.group.service

import com.yapp.brake.group.dto.request.CreateGroupRequest
import com.yapp.brake.group.dto.request.UpdateGroupRequest
import com.yapp.brake.group.dto.response.GroupResponse
import com.yapp.brake.group.dto.response.GroupsResponse

interface GroupUseCase {
    fun create(
        deviceProfileId: Long,
        request: CreateGroupRequest,
    ): GroupResponse

    fun getAll(deviceProfileId: Long): GroupsResponse

    fun modify(
        deviceProfileId: Long,
        groupId: Long,
        request: UpdateGroupRequest,
    ): GroupResponse

    fun remove(
        deviceProfileId: Long,
        groupId: Long,
    )
}
