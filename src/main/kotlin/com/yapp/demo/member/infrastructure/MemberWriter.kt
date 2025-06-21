package com.yapp.demo.member.infrastructure

import com.yapp.demo.member.infrastructure.jpa.MemberEntity
import com.yapp.demo.member.infrastructure.jpa.MemberRepository
import com.yapp.demo.member.model.Member
import org.springframework.stereotype.Repository

@Repository
class MemberWriter(
    private val memberRepository: MemberRepository,
) {
    fun save(member: Member): Member {
        val entity = MemberEntity.from(member)
        return memberRepository.save(entity).toDomain()
    }
}
