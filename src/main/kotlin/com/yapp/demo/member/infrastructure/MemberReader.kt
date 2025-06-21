package com.yapp.demo.member.infrastructure

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.member.infrastructure.jpa.MemberRepository
import com.yapp.demo.member.model.Member
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class MemberReader(
    private val memberRepository: MemberRepository,
) {
    fun findById(memberId: Long): Member? =
        memberRepository.findById(memberId)
            .getOrNull()
            ?.toDomain()

    fun getById(memberId: Long): Member =
        memberRepository.findById(memberId)
            .getOrNull()
            ?.toDomain()
            ?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)

    fun findByAuthEmail(email: String): Member? = memberRepository.findByAuthEmail(email)?.toDomain()
}
