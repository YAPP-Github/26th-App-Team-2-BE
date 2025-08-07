package com.yapp.brake.group.infrastructure.jpa

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.group.infrastructure.GroupReader
import com.yapp.brake.group.model.Group
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class GroupJpaReader(
    private val groupRepository: GroupRepository,
) : GroupReader {
    override fun getByIdAndDeviceProfileId(
        groupId: Long,
        deviceProfileId: Long,
    ): Group {
        return groupRepository.findByGroupIdAndDeviceProfileId(groupId, deviceProfileId)
            ?.toDomain()
            ?: throw CustomException(ErrorCode.GROUP_NOT_FOUND)
    }

    override fun getAllByDeviceProfileId(deviceProfileId: Long): List<Group> =
        groupRepository.findByDeviceProfileId(deviceProfileId)
            .map(GroupEntity::toDomain)
}
