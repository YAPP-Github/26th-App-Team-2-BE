package com.yapp.brake.group.service

import com.yapp.brake.group.dto.response.GroupResponse
import com.yapp.brake.group.infrastructure.GroupReader
import com.yapp.brake.group.infrastructure.GroupWriter
import com.yapp.brake.group.model.Group
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GroupService(
    private val groupWriter: GroupWriter,
    private val groupReader: GroupReader,
) : GroupUseCase {
    @Transactional
    override fun create(
        memberId: Long,
        name: String,
    ): GroupResponse {
        val group = Group.create(memberId, name)
        val savedGroup = groupWriter.save(group)

        return GroupResponse(savedGroup.groupId, savedGroup.name)
    }

    @Transactional
    override fun modify(
        memberId: Long,
        groupId: Long,
        name: String,
    ): GroupResponse {
        val group = groupReader.getByIdAndMemberId(groupId, memberId)
        val updatedGroup = groupWriter.save(group.update(name))

        return GroupResponse(updatedGroup.groupId, updatedGroup.name)
    }

    @Transactional
    override fun remove(
        memberId: Long,
        groupId: Long,
    ) {
        val group = groupReader.getByIdAndMemberId(groupId, memberId)
        groupWriter.delete(group)
    }
}
