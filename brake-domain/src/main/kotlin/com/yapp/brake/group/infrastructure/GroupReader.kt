package com.yapp.brake.group.infrastructure

import com.yapp.brake.group.model.Group

interface GroupReader {
    fun getByIdAndDeviceProfileId(
        groupId: Long,
        deviceProfileId: Long,
    ): Group

    fun getAllByDeviceProfileId(deviceProfileId: Long): List<Group>
}
