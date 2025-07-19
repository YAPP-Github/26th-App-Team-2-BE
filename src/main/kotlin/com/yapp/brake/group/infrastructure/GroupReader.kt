package com.yapp.brake.group.infrastructure

import com.yapp.brake.group.model.Group

interface GroupReader {
    fun getByIdAndMemberId(
        groupId: Long,
        memberId: Long,
    ): Group

    fun getAllInfiniteScroll(
        groupId: Long,
        limit: Long,
        lastGroupId: Long?,
    ): List<Group>
}
