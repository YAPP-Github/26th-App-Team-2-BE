package com.yapp.brake.groupapp.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GroupAppRepository : JpaRepository<GroupAppEntity, Long> {
    fun findByGroupId(groupId: Long): List<GroupAppEntity>

    @Modifying
    @Query("delete from GroupAppEntity g where g.groupId = :groupId")
    fun deleteByGroupId(
        @Param("groupId") groupId: Long,
    ): Int

    fun deleteByGroupAppIdIn(groupAppIds: List<Long>)
}
