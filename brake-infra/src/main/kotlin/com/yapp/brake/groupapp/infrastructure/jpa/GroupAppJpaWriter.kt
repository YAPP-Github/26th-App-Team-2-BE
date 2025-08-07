package com.yapp.brake.groupapp.infrastructure.jpa

import com.yapp.brake.groupapp.infrastructure.GroupAppWriter
import com.yapp.brake.groupapp.model.GroupApp
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class GroupAppJpaWriter(
    private val groupAppRepository: GroupAppRepository,
) : GroupAppWriter {
    override fun save(groupApp: GroupApp): GroupApp {
        val entity = GroupAppEntity.create(groupApp)
        return groupAppRepository.save(entity).toDomain()
    }

    override fun delete(groupAppId: Long) {
        groupAppRepository.deleteById(groupAppId)
    }

    override fun deleteByGroupId(groupId: Long) {
        groupAppRepository.deleteByGroupId(groupId)
    }

    override fun deleteByGroupAppIds(groupAppIds: List<Long>) {
        groupAppRepository.deleteByGroupAppIdIn(groupAppIds)
    }
}
