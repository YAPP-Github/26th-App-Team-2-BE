package com.yapp.brake.group.infrastructure

import com.yapp.brake.group.model.Group

interface GroupReader {
    fun getById(groupId: Long): Group
}
