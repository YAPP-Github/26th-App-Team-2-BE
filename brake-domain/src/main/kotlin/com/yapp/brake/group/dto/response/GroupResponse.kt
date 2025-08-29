package com.yapp.brake.group.dto.response

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.common.serializer.DataSerializer.serialize
import com.yapp.brake.group.model.Group
import com.yapp.brake.groupapp.model.GroupApp

data class GroupResponse(
    val groupId: Long,
    val name: String,
    val groupApps: List<GroupAppResponse> = emptyList(),
) {
    fun toIosResponse(): GroupIosResponse {
        val groupAppsJsonString =
            serialize(groupApps)
                ?: throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)

        return GroupIosResponse(
            groupId = groupId,
            name = name,
            groupApps = groupAppsJsonString,
        )
    }

    companion object {
        fun from(
            group: Group,
            groupApps: List<GroupApp>,
        ) = GroupResponse(
            groupId = group.groupId,
            name = group.name,
            groupApps = groupApps.map(GroupAppResponse::from),
        )
    }
}
