package com.yapp.brake.member.infrastructure.jpa

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.member.infrastructure.MemberReader
import com.yapp.brake.member.model.Member
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Repository
@Transactional(readOnly = true)
class MemberJpaReader(
    private val memberRepository: MemberRepository,
) : MemberReader {
    override fun findByDeviceId(deviceId: String): Member? =
        memberRepository.findByDeviceId(deviceId)
            ?.toDomain()

    override fun findById(memberId: Long): Member? =
        memberRepository.findById(memberId)
            .getOrNull()
            ?.toDomain()

    override fun getById(memberId: Long): Member =
        memberRepository.findById(memberId)
            .getOrNull()
            ?.toDomain()
            ?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)
}
