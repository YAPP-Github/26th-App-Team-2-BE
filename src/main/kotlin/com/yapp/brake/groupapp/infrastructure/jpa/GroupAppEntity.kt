package com.yapp.brake.groupapp.infrastructure.jpa

import com.yapp.brake.common.persistence.Auditable
import com.yapp.brake.groupapp.model.GroupApp
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "group_app")
class GroupAppEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val groupAppId: Long = 0L,
    val groupId: Long,
    val packageName: String,
    val name: String,
) : Auditable() {
    fun toDomain() =
        GroupApp(
            groupAppId = groupAppId,
            groupId = groupId,
            packageName = packageName,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun create(groupApp: GroupApp) =
            GroupAppEntity(
                groupId = groupApp.groupId,
                packageName = groupApp.packageName,
                name = groupApp.name,
            )
    }
}
