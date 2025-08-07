package com.yapp.brake.group.dto.response

import com.yapp.brake.group.model.Group
import com.yapp.brake.groupapp.model.GroupApp

data class GroupResponse(
    val groupId: Long,
    val name: String,
    val groupApps: List<GroupAppResponse> = emptyList(),
) {
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
