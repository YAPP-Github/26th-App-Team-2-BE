package com.yapp.demo.member.service

import com.yapp.demo.common.security.getMemberId
import com.yapp.demo.member.dto.response.MemberResponse
import com.yapp.demo.member.infrastructure.MemberReader
import com.yapp.demo.member.infrastructure.MemberWriter
import com.yapp.demo.member.model.MemberState
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberReader: MemberReader,
    private val memberWriter: MemberWriter,
) : MemberUseCase {
    @Transactional(readOnly = true)
    override fun getMember(memberId: Long): MemberResponse = MemberResponse.from(memberReader.getById(memberId))

    @Transactional
    override fun update(nickname: String): MemberResponse {
        val member =
            memberReader.getById(getMemberId())
                .update(nickname, MemberState.ACTIVE)

        return MemberResponse.from(memberWriter.save(member))
    }

    @Transactional
    override fun remove(memberId: Long) {
        memberWriter.remove(memberReader.getById(memberId))
    }
}
