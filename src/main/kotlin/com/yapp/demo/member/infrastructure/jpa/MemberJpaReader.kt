package com.yapp.demo.member.infrastructure.jpa

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.member.infrastructure.MemberReader
import com.yapp.demo.member.model.Member
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class MemberJpaReader(
    private val memberRepository: MemberRepository,
) : MemberReader {
    override fun findById(memberId: Long): Member? =
        memberRepository.findById(memberId)
            .getOrNull()
            ?.toDomain()

    override fun getById(memberId: Long): Member =
        memberRepository.findById(memberId)
            .getOrNull()
            ?.toDomain()
            ?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)

    override fun findByAuthEmail(email: String): Member? = memberRepository.findByAuthEmail(email)?.toDomain()
}
