package com.yapp.brake.groupapp.service

import com.yapp.brake.groupapp.dto.response.AddGroupAppResponse
import com.yapp.brake.groupapp.infrastructure.GroupAppWriter
import com.yapp.brake.groupapp.model.GroupApp
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GroupAppService(
    private val groupAppWriter: GroupAppWriter,
) : GroupAppUseCase {
    @Transactional
    override fun add(
        groupId: Long,
        name: String,
    ): AddGroupAppResponse {
        val groupApp = GroupApp.create(groupId = groupId, name = name)
        val savedGroupApp = groupAppWriter.save(groupApp)

        return AddGroupAppResponse(savedGroupApp.groupAppId)
    }

    @Transactional
    override fun remove(groupAppId: Long) {
        groupAppWriter.deleteByGroupId(groupAppId)
    }
}
