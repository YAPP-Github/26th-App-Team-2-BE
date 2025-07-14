package com.yapp.brake.group.service

import com.yapp.brake.group.dto.response.GroupResponse
import com.yapp.brake.group.infrastructure.GroupReader
import com.yapp.brake.group.infrastructure.GroupWriter
import com.yapp.brake.group.model.Group
import org.springframework.transaction.annotation.Transactional

class GroupService(
    private val groupWriter: GroupWriter,
    private val groupReader: GroupReader,
) : GroupUseCase {
    @Transactional
    override fun create(name: String): GroupResponse {
        val group = Group.create(name)
        val savedGroup = groupWriter.save(group)

        return GroupResponse(savedGroup.groupId, savedGroup.name)
    }

    @Transactional
    override fun modify(
        groupId: Long,
        name: String,
    ): GroupResponse {
        val group = groupReader.getById(groupId)
        val updatedGroup = groupWriter.save(group.update(name))

        return GroupResponse(updatedGroup.groupId, updatedGroup.name)
    }

    @Transactional
    override fun remove(groupId: Long) {
        groupWriter.delete(groupId)
    }
}
