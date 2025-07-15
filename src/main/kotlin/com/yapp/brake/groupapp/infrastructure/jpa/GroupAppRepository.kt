package com.yapp.brake.groupapp.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupAppRepository : JpaRepository<GroupAppEntity, Long> {
    fun findByGroupId(groupId: Long): List<GroupAppEntity>
}
