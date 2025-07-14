package com.yapp.brake.group.service

import com.yapp.brake.group.dto.response.GroupResponse

interface GroupUseCase {
    fun create(
        memberId: Long,
        name: String,
    ): GroupResponse

    fun modify(
        memberId: Long,
        groupId: Long,
        name: String,
    ): GroupResponse

    fun remove(
        memberId: Long,
        groupId: Long,
    )
}
