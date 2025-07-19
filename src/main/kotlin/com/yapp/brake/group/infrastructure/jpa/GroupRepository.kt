package com.yapp.brake.group.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<GroupEntity, Long> {
    fun findByGroupIdAndMemberId(
        groupId: Long,
        memberId: Long,
    ): GroupEntity?
}
