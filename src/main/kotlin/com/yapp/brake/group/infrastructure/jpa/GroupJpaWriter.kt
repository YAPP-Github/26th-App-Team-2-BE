package com.yapp.brake.group.infrastructure.jpa

import com.yapp.brake.group.infrastructure.GroupWriter
import com.yapp.brake.group.model.Group
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class GroupJpaWriter(
    private val groupRepository: GroupRepository,
) : GroupWriter {
    override fun save(group: Group): Group {
        val entity = GroupEntity.create(group)
        return groupRepository.save(entity).toDomain()
    }

    override fun delete(groupId: Long) {
        groupRepository.deleteById(groupId)
    }
}
