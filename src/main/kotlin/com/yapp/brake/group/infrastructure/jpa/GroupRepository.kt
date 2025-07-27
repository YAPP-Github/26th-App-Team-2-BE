package com.yapp.brake.group.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<GroupEntity, Long> {
    fun findByGroupIdAndDeviceProfileId(
        groupId: Long,
        deviceProfileId: Long,
    ): GroupEntity?

    fun findByDeviceProfileId(deviceProfileId: Long): List<GroupEntity>
}
