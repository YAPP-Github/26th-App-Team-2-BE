package com.yapp.brake.support.fixture.dto.group

import com.yapp.brake.group.dto.response.GroupResponse
import com.yapp.brake.group.dto.response.GroupsResponse

fun groupsResponseFixture() =
    GroupsResponse(
        listOf(
            GroupResponse(
                groupId = 1L,
                name = "그룹1",
                groupApps =
                    List(4) { i ->
                        groupAppResponseFixture(i + 1L, "앱$i")
                    },
            ),
            GroupResponse(
                groupId = 2L,
                name = "그룹2",
                groupApps =
                    List(3) { i ->
                        groupAppResponseFixture(i + 10L, "앱${i + 10}")
                    },
            ),
            GroupResponse(
                groupId = 3L,
                name = "그룹1",
                groupApps =
                    List(2) { i ->
                        groupAppResponseFixture(i + 100L, "앱${i + 100}")
                    },
            ),
        ),
    )
