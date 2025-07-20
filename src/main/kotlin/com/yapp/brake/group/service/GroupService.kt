package com.yapp.brake.group.service

import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.GroupDeletedEventPayload
import com.yapp.brake.common.event.payload.GroupUpdatedEventPayload
import com.yapp.brake.group.dto.request.CreateGroupRequest
import com.yapp.brake.group.dto.request.UpdateGroupRequest
import com.yapp.brake.group.dto.response.GroupResponse
import com.yapp.brake.group.infrastructure.GroupReader
import com.yapp.brake.group.infrastructure.GroupWriter
import com.yapp.brake.group.model.Group
import com.yapp.brake.groupapp.infrastructure.GroupAppReader
import com.yapp.brake.groupapp.infrastructure.GroupAppWriter
import com.yapp.brake.groupapp.model.GroupApp
import com.yapp.brake.outbox.infrastructure.event.OutboxEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GroupService(
    private val groupWriter: GroupWriter,
    private val groupReader: GroupReader,
    private val groupAppWriter: GroupAppWriter,
    private val groupAppReader: GroupAppReader,
    private val outboxEventPublisher: OutboxEventPublisher,
) : GroupUseCase {
    @Transactional
    override fun create(
        memberId: Long,
        request: CreateGroupRequest,
    ): GroupResponse {
        val group = groupWriter.save(Group.create(memberId, request.name))
        val groupApps =
            request.groupApps.map {
                val groupApp =
                    GroupApp.create(
                        groupId = group.groupId,
                        name = it.name,
                    )

                groupAppWriter.save(groupApp)
            }

        return GroupResponse.from(group, groupApps)
    }

    @Transactional(readOnly = true)
    override fun getAll(memberId: Long): List<GroupResponse> {
        val groups = groupReader.getAllByMemberId(memberId)
        val groupIds = groups.map(Group::groupId)
        val groupApps = groupAppReader.getByGroupIds(groupIds)
        val groupAppMap: Map<Long, List<GroupApp>> = groupApps.groupBy { it.groupId }

        return groups.map { group ->
            val apps = groupAppMap[group.groupId] ?: emptyList()
            GroupResponse.from(group, apps)
        }
    }

    @Transactional
    override fun modify(
        memberId: Long,
        groupId: Long,
        request: UpdateGroupRequest,
    ): GroupResponse {
        val group =
            groupReader.getByIdAndMemberId(groupId, memberId)
                .update(request.name)
                .let { groupWriter.save(it) }

        val originGroupApps = groupAppReader.getByGroupId(groupId)
        val updatedGroupApps =
            request.groupApps
                .map { GroupApp.create(it.groupAppId, groupId, it.name) }

        val existed = findAppsToKeep(originGroupApps, updatedGroupApps)
        val added = findNewApps(updatedGroupApps).map(groupAppWriter::save)
        val deleted = findAppsToDelete(originGroupApps, updatedGroupApps)

        if (deleted.isNotEmpty()) {
            val payload = GroupUpdatedEventPayload(deleted.map(GroupApp::groupAppId))
            outboxEventPublisher.publish(EventType.GROUP_UPDATED, payload)
        }

        return GroupResponse.from(group, existed + added)
    }

    @Transactional
    override fun remove(
        memberId: Long,
        groupId: Long,
    ) {
        val group = groupReader.getByIdAndMemberId(groupId, memberId)

        groupWriter.delete(group)

        val payload = GroupDeletedEventPayload(group.groupId)

        outboxEventPublisher.publish(EventType.GROUP_DELETED, payload)
    }

    private fun findAppsToKeep(
        origin: List<GroupApp>,
        updated: List<GroupApp>,
    ): List<GroupApp> =
        origin.filter { o ->
            updated.any { u -> o.groupAppId == u.groupAppId }
        }

    private fun findNewApps(updated: List<GroupApp>): List<GroupApp> = updated.filter(GroupApp::isNew)

    private fun findAppsToDelete(
        origin: List<GroupApp>,
        updated: List<GroupApp>,
    ): List<GroupApp> =
        origin.filter { o ->
            updated.none { u -> !u.isNew() && o.groupAppId == u.groupAppId }
        }
}
