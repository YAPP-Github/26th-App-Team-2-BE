package com.yapp.brake.groupapp.model

import java.time.LocalDateTime

data class GroupApp(
    val groupAppId: Long,
    val groupId: Long,
    val name: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    fun isNew() = groupAppId == 0L

    companion object {
        fun create(
            groupAppId: Long = 0L,
            groupId: Long,
            name: String,
        ) = GroupApp(
            groupAppId = groupAppId,
            groupId = groupId,
            name = name,
        )
    }
}
