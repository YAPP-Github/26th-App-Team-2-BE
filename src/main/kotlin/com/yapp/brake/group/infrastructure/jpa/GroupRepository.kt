package com.yapp.brake.group.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface GroupRepository : JpaRepository<GroupEntity, Long> {
    fun findByGroupIdAndMemberId(
        groupId: Long,
        memberId: Long,
    ): GroupEntity?

    @Query(
        value =
            "select group_id, member_id, name, created_at, update_at " +
                "from group " +
                "where memberId = :memberId " +
                "order by group_id limit :limit",
        nativeQuery = true,
    )
    fun findAllInfiniteScroll(
        @Param("groupId") groupId: Long,
        @Param("limit") limit: Long,
    ): List<GroupEntity>

    @Query(
        value =
            "select group_id, member_id, name, created_at, update_at " +
                "from group " +
                "where memberId = :memberId and group_id > :lastGroupId " +
                "order by group_id limit :limit",
        nativeQuery = true,
    )
    fun findAllInfiniteScroll(
        @Param("groupId") groupId: Long,
        @Param("limit") limit: Long,
        @Param("lastGroupId") lastGroupId: Long,
    ): List<GroupEntity>
}
