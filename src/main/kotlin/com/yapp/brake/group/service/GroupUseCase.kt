package com.yapp.brake.group.service

import com.yapp.brake.group.dto.response.CreateGroupResponse

interface GroupUseCase {
    fun create(name: String): CreateGroupResponse

    fun remove(groupId: Long)
}
