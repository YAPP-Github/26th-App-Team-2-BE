package com.yapp.brake.groupapp.model

import java.time.LocalDateTime

data class GroupApp(
    val groupAppId: Long,
    val groupId: Long,
    val appId: String,
    val createdAt: LocalDateTime? = null,
) {
    companion object {
        fun create(
            groupId: Long,
            appId: String,
        ) = GroupApp(
            groupAppId = 0L,
            groupId = groupId,
            appId = appId,
        )
    }
}
