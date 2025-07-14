package com.yapp.brake.groupapp.infrastructure

import com.yapp.brake.groupapp.model.GroupApp

interface GroupAppWriter {
    fun save(groupApp: GroupApp): GroupApp

    fun remove(groupAppId: Long)
}
