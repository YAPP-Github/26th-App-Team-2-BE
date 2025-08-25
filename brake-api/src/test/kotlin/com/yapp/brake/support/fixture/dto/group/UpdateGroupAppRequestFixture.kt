package com.yapp.brake.support.fixture.dto.group

import com.yapp.brake.group.dto.request.UpdateGroupAppRequest

fun updateGroupAppRequestFixture(
    groupAppId: Long? = null,
    name: String = "카카오톡",
    packageName: String = "package_name",
) = UpdateGroupAppRequest(
    groupAppId = groupAppId,
    name = name,
    packageName = packageName,
)
