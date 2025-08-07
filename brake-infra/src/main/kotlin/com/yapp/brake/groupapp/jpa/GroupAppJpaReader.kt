package com.yapp.brake.groupapp.jpa

import com.yapp.brake.groupapp.model.GroupApp
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class GroupAppJpaReader(
    private val groupAppRepository: GroupAppRepository,
) : GroupAppReader {
    override fun getByGroupId(groupId: Long): List<GroupApp> =
        groupAppRepository.findByGroupId(groupId)
            .map(GroupAppEntity::toDomain)

    override fun getByGroupIds(groupIds: List<Long>): List<GroupApp> =
        groupAppRepository.findByGroupIdIn(groupIds)
            .map(GroupAppEntity::toDomain)
}
