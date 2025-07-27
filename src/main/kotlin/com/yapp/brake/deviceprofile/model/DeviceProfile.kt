package com.yapp.brake.deviceprofile.model

data class DeviceProfile(
    val id: Long = 0L,
    val memberId: Long,
    val deviceName: String,
) {
    companion object {
        fun create(
            memberId: Long,
            deviceName: String,
        ): DeviceProfile {
            return DeviceProfile(
                memberId = memberId,
                deviceName = deviceName,
            )
        }
    }
}
