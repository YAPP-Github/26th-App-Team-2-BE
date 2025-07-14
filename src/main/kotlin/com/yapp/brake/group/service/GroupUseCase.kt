package com.yapp.brake.group.service

import com.yapp.brake.group.dto.response.GroupResponse

interface GroupUseCase {
    fun create(name: String): GroupResponse

    fun modify(
        groupId: Long,
        name: String,
    ): GroupResponse

    fun remove(groupId: Long)
}
