package com.yapp.brake.group.dto.response

import com.yapp.brake.group.model.Group
import com.yapp.brake.groupapp.model.GroupApp

data class GroupsResponse(
    val groups: List<GroupResponse>,
) {
    companion object {
        fun from(
            groups: List<Group>,
            groupApps: List<GroupApp>,
        ): GroupsResponse {
            val groupAppMap: Map<Long, List<GroupApp>> = groupApps.groupBy { it.groupId }

            return GroupsResponse(
                groups.map { group ->
                    val apps = groupAppMap[group.groupId] ?: emptyList()
                    GroupResponse.from(group, apps)
                },
            )
        }
    }
}
