package com.yapp.brake.groupapp.infrastructure.jpa

import com.yapp.brake.groupapp.infrastructure.GroupAppReader
import com.yapp.brake.groupapp.model.GroupApp
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class GroupAppJpaReader(
    private val groupAppRepository: GroupAppRepository,
) : GroupAppReader {
    override fun getByGroupId(groupId: Long): List<GroupApp> =
        groupAppRepository.findByGroupId(groupId)
            .map(GroupAppEntity::toDomain)
}
