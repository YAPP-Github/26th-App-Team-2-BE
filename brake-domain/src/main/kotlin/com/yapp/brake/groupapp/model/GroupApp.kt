package com.yapp.brake.groupapp.model

import java.time.LocalDateTime

data class GroupApp(
    val groupAppId: Long,
    val groupId: Long,
    val packageName: String,
    val name: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    fun isNew() = groupAppId == 0L

    companion object {
        fun create(
            groupAppId: Long? = null,
            groupId: Long,
            packageName: String,
            name: String,
        ) = GroupApp(
            groupAppId = groupAppId ?: 0L,
            groupId = groupId,
            packageName = packageName,
            name = name,
        )
    }
}
