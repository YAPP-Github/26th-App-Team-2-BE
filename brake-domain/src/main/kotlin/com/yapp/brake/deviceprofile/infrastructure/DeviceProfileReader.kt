package com.yapp.brake.deviceprofile.infrastructure

import com.yapp.brake.deviceprofile.model.DeviceProfile

interface DeviceProfileReader {
    fun getById(deviceProfileId: Long): DeviceProfile

    fun findByMemberIdAndDeviceName(
        memberId: Long,
        deviceName: String,
    ): DeviceProfile?
}
