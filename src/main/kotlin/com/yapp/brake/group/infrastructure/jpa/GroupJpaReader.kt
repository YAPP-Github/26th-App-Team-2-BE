package com.yapp.brake.group.infrastructure.jpa

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.group.infrastructure.GroupReader
import com.yapp.brake.group.model.Group
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Repository
@Transactional(readOnly = true)
class GroupJpaReader(
    private val groupRepository: GroupRepository,
) : GroupReader {
    override fun getById(groupId: Long): Group {
        return groupRepository.findById(groupId)
            .getOrNull()
            ?.toDomain()
            ?: throw CustomException(ErrorCode.GROUP_NOT_FOUND)
    }
}
