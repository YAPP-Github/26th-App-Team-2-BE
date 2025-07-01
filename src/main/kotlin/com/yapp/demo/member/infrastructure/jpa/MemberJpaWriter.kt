package com.yapp.demo.member.infrastructure.jpa

import com.yapp.demo.member.infrastructure.MemberWriter
import com.yapp.demo.member.model.Member
import org.springframework.stereotype.Repository

@Repository
class MemberJpaWriter(
    private val memberRepository: MemberRepository,
) : MemberWriter {
    override fun save(member: Member): Member {
        val entity = MemberEntity.from(member)
        return memberRepository.save(entity).toDomain()
    }

    override fun delete(memberId: Long) {
        memberRepository.deleteById(memberId)
    }
}
