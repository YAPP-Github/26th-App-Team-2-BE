package com.yapp.brake.deviceprofile.jpa

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.deviceprofile.model.DeviceProfile
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class DeviceProfileJpaReader(
    private val deviceProfileRepository: DeviceProfileRepository,
) : DeviceProfileReader {
    override fun getById(deviceProfileId: Long): DeviceProfile =
        deviceProfileRepository.findById(deviceProfileId)
            .orElseThrow { CustomException(ErrorCode.DEVICE_PROFILE_NOT_FOUND) }
            .toDomain()

    override fun findByMemberIdAndDeviceName(
        memberId: Long,
        deviceName: String,
    ): DeviceProfile? =
        deviceProfileRepository.findByMemberIdAndDeviceName(memberId, deviceName)
            ?.toDomain()
}
