package com.yapp.brake.group.dto.response

import com.yapp.brake.groupapp.model.GroupApp

data class GroupAppResponse(
    val groupAppId: Long,
    val packageName: String,
    val name: String,
) {
    companion object {
        fun from(groupApp: GroupApp) =
            GroupAppResponse(
                groupAppId = groupApp.groupAppId,
                packageName = groupApp.packageName,
                name = groupApp.name,
            )
    }
}
