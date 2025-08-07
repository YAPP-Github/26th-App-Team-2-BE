package com.yapp.brake.member.jpa

import com.yapp.brake.member.infrastructure.MemberWriter
import com.yapp.brake.member.model.Member
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class MemberJpaWriter(
    private val memberRepository: MemberRepository,
) : MemberWriter {
    override fun save(member: Member): Member {
        val entity = MemberEntity.create(member)
        return memberRepository.save(entity).toDomain()
    }

    override fun delete(memberId: Long) {
        memberRepository.deleteById(memberId)
    }
}
