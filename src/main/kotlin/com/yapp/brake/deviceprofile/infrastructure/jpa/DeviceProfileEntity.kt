package com.yapp.brake.deviceprofile.infrastructure.jpa

import com.yapp.brake.common.persistence.Auditable
import com.yapp.brake.deviceprofile.model.DeviceProfile
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "device_profile")
class DeviceProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val deviceProfileId: Long = 0L,
    val deviceName: String,
    val memberId: Long,
) : Auditable() {
    fun toDomain(): DeviceProfile {
        return DeviceProfile(
            id = deviceProfileId,
            deviceName = deviceName,
            memberId = memberId,
        )
    }

    companion object {
        fun create(deviceProfile: DeviceProfile): DeviceProfileEntity {
            return DeviceProfileEntity(
                deviceProfileId = deviceProfile.id,
                deviceName = deviceProfile.deviceName,
                memberId = deviceProfile.memberId,
            )
        }
    }
}
