package com.yapp.brake.member.service

import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.MemberDeletedEventPayload
import com.yapp.brake.member.dto.response.MemberResponse
import com.yapp.brake.member.infrastructure.MemberReader
import com.yapp.brake.member.infrastructure.MemberWriter
import com.yapp.brake.member.model.MemberState
import com.yapp.brake.outbox.infrastructure.event.OutboxEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberReader: MemberReader,
    private val memberWriter: MemberWriter,
    private val outboxEventPublisher: OutboxEventPublisher,
) : MemberUseCase {
    @Transactional(readOnly = true)
    override fun getMember(memberId: Long): MemberResponse = MemberResponse.from(memberReader.getById(memberId))

    @Transactional
    override fun update(
        memberId: Long,
        nickname: String,
    ): MemberResponse {
        val member =
            memberReader.getById(memberId)
                .update(nickname, MemberState.ACTIVE)

        return MemberResponse.from(memberWriter.save(member))
    }

    @Transactional
    override fun delete(memberId: Long) {
        val member = memberReader.getById(memberId)
        memberWriter.delete(memberId)
        // todo : 회원 탈퇴 시 회원의 디바이스 프로필도 삭제해야 함

        val payload =
            MemberDeletedEventPayload(
                memberId = member.id,
                socialProvider = member.oAuthUserInfo.socialProvider.name,
                authId = member.oAuthUserInfo.credential,
                authEmail = member.oAuthUserInfo.email,
            )

        outboxEventPublisher.publish(EventType.MEMBER_DELETED, payload)
    }
}
