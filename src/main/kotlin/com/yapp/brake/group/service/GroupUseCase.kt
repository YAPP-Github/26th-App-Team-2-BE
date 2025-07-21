package com.yapp.brake.group.service

import com.yapp.brake.group.dto.request.CreateGroupRequest
import com.yapp.brake.group.dto.request.UpdateGroupRequest
import com.yapp.brake.group.dto.response.GroupResponse
import com.yapp.brake.group.dto.response.GroupsResponse

interface GroupUseCase {
    fun create(
        memberId: Long,
        request: CreateGroupRequest,
    ): GroupResponse

    fun getAll(memberId: Long): GroupsResponse

    fun modify(
        memberId: Long,
        groupId: Long,
        request: UpdateGroupRequest,
    ): GroupResponse

    fun remove(
        memberId: Long,
        groupId: Long,
    )
}
