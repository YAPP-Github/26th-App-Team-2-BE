package com.yapp.brake.deviceprofile.infrastructure

import com.yapp.brake.deviceprofile.model.DeviceProfile

interface DeviceProfileWriter {
    fun save(deviceProfile: DeviceProfile): DeviceProfile

    fun deleteAllByMemberId(memberId: Long): Long
}
