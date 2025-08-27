package com.yapp.brake.support.fixture.dto.group

import com.yapp.brake.group.dto.request.AddGroupAppRequest

fun addGroupAppRequestFixture(
    name: String = "카카오톡",
    packageName: String = "package_name",
) = AddGroupAppRequest(
    name = name,
    packageName = packageName,
)
