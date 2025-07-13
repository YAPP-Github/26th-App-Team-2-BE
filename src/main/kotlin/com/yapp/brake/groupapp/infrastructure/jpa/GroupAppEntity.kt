package com.yapp.brake.groupapp.infrastructure.jpa

import com.yapp.brake.groupapp.model.GroupApp
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "group_app")
class GroupAppEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val groupAppId: Long = 0L,
    val groupId: Long,
    val appId: String,
    val createdAt: LocalDateTime,
) {
    fun toDomain() =
        GroupApp(
            groupAppId = groupAppId,
            groupId = groupId,
            appId = appId,
            createdAt = createdAt,
        )

    companion object {
        fun create(groupApp: GroupApp) =
            GroupAppEntity(
                groupId = groupApp.groupId,
                appId = groupApp.appId,
                createdAt = LocalDateTime.now(),
            )
    }
}
