package com.yapp.brake.support.fixture.model

import com.yapp.brake.deviceprofile.model.DeviceProfile

fun deviceProfileFixture(
    id: Long = 1L,
    memberId: Long = 1L,
    deviceName: String = "Test Device",
) = DeviceProfile(
    id = id,
    memberId = memberId,
    deviceName = deviceName,
)
