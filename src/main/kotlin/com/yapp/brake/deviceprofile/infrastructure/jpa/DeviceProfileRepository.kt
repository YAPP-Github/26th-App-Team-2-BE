package com.yapp.brake.deviceprofile.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface DeviceProfileRepository : JpaRepository<DeviceProfileEntity, Long> {
    fun findByMemberIdAndDeviceName(
        memberId: Long,
        deviceName: String,
    ): DeviceProfileEntity?
}
