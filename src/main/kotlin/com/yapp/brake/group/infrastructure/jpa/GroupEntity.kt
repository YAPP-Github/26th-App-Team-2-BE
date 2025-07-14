package com.yapp.brake.group.infrastructure.jpa

import com.yapp.brake.common.persistence.Auditable
import com.yapp.brake.group.model.Group
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "group")
class GroupEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val groupId: Long = 0L,
    val name: String,
) : Auditable() {
    fun toDomain() =
        Group(
            groupId = groupId,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun create(group: Group) =
            GroupEntity(
                name = group.name,
            )
    }
}
