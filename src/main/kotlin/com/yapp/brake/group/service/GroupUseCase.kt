package com.yapp.brake.group.service

import com.yapp.brake.group.dto.request.CreateGroupRequest
import com.yapp.brake.group.dto.request.UpdateGroupRequest
import com.yapp.brake.group.dto.response.GroupResponse

interface GroupUseCase {
    fun create(
        memberId: Long,
        request: CreateGroupRequest,
    ): GroupResponse

    fun getAll(
        memberId: Long,
        pageSize: Long,
        lastGroupId: Long?,
    ): List<GroupResponse>

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
