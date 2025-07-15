package com.yapp.brake.groupapp.infrastructure

import com.yapp.brake.groupapp.model.GroupApp

interface GroupAppReader {
    fun getByGroupId(groupId: Long): List<GroupApp>
}
