package com.yapp.brake.deviceprofile.jpa

import com.yapp.brake.deviceprofile.model.DeviceProfile
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class DeviceProfileJpaWriter(
    private val deviceProfileRepository: DeviceProfileRepository,
) : DeviceProfileWriter {
    override fun save(deviceProfile: DeviceProfile): DeviceProfile {
        val entity = DeviceProfileEntity.create(deviceProfile)
        return deviceProfileRepository.save(entity).toDomain()
    }

    override fun deleteAllByMemberId(memberId: Long): Long {
        return deviceProfileRepository.deleteAllByMemberId(memberId)
    }
}
