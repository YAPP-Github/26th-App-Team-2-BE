package com.yapp.brake.groupapp.infrastructure

import com.yapp.brake.groupapp.model.GroupApp

interface GroupAppWriter {
    fun save(groupApp: GroupApp): GroupApp

    fun delete(groupAppId: Long)

    fun deleteByGroupId(groupId: Long)

    fun deleteByGroupAppIds(groupAppIds: List<Long>)
}
