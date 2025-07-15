package com.yapp.brake.group.infrastructure.jpa

import com.yapp.brake.group.infrastructure.GroupWriter
import com.yapp.brake.group.model.Group
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class GroupJpaWriter(
    private val groupRepository: GroupRepository,
) : GroupWriter {
    override fun save(group: Group): Group {
        val entity = GroupEntity.from(group)
        return groupRepository.save(entity).toDomain()
    }

    override fun delete(group: Group) {
        groupRepository.delete(GroupEntity.from(group))
    }
}
