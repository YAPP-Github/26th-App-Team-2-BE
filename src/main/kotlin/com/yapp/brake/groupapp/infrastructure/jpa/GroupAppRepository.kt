package com.yapp.brake.groupapp.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying

interface GroupAppRepository : JpaRepository<GroupAppEntity, Long> {
    fun findByGroupId(groupId: Long): List<GroupAppEntity>

    @Modifying
    fun deleteByGroupId(groupId: Long): Int

    fun deleteByGroupAppIdIn(groupAppIds: List<Long>)
}
