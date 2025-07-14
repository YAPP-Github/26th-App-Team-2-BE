package com.yapp.brake.groupapp.service

import com.yapp.brake.groupapp.dto.response.AddGroupAppResponse

interface GroupAppUseCase {
    fun add(
        groupId: Long,
        appId: String,
    ): AddGroupAppResponse

    fun remove(groupAppId: Long)
}
