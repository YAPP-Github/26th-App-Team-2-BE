package com.yapp.brake.member.service

import com.yapp.brake.common.event.EventType
import com.yapp.brake.common.event.payload.MemberDeletedEventPayload
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.member.infrastructure.MemberReader
import com.yapp.brake.member.infrastructure.MemberWriter
import com.yapp.brake.member.model.MemberState
import com.yapp.brake.outbox.infrastructure.event.OutboxEventPublisher
import com.yapp.brake.support.fixture.model.memberFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class MemberServiceTest {
    private val memberReader = mock<MemberReader>()
    private val memberWriter = mock<MemberWriter>()
    private val outboxEventPublisher = mock<OutboxEventPublisher>()
    private val memberService = MemberService(memberReader, memberWriter, outboxEventPublisher)

    @Nested
    inner class GetMemberTest {
        @Test
        fun `getMember()는 유저가 존재하면 식별자로 조회가 가능하다`() {
            val expected = memberFixture(id = 1L, nickname = "brake-user")

            whenever(memberReader.getById(memberId = expected.id)).thenReturn(expected)

            val result = memberService.getMember(expected.id)

            assertThat(result.nickname).isEqualTo(expected.nickname)
        }

        @Test
        fun `getMember()는 유저가 존재하지 않으면 예외를 던진다`() {
            val invalidId = 12124124L
            whenever(memberReader.getById(invalidId)).thenThrow(CustomException(ErrorCode.MEMBER_NOT_FOUND))

            val result =
                assertThrows<CustomException> {
                    memberService.getMember(invalidId)
                }

            assertThat(result.errorCode).isEqualTo(ErrorCode.MEMBER_NOT_FOUND)
        }
    }

    @Test
    fun `update()는 유저가 존재하면 유저 정보를 변경한다`() {
        val newNickname = "brake-user-modified"
        val member = memberFixture(id = 1L, nickname = "brake-user", state = MemberState.HOLD)
        val expected = memberFixture(id = member.id, nickname = newNickname)

        // SecurityContext 설정
        val authentication = UsernamePasswordAuthenticationToken(member.id.toString(), null)
        SecurityContextHolder.getContext().authentication = authentication

        whenever(memberReader.getById(memberId = member.id)).thenReturn(member)
        whenever(memberWriter.save(any())).thenReturn(expected)

        val result = memberService.update(newNickname)

        assertThat(result.nickname).isEqualTo(expected.nickname)
        assertThat(result.state).isEqualTo(MemberState.ACTIVE.name)

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `delete()는 유저 삭제하고 이벤트를 발행한다`() {
        val member = memberFixture(id = 1L, nickname = "brake-user", state = MemberState.HOLD)
        val payload =
            MemberDeletedEventPayload(
                memberId = member.id,
                socialProvider = member.oAuthUserInfo.socialProvider.name,
                authId = member.oAuthUserInfo.credential,
                authEmail = member.oAuthUserInfo.email,
                deviceId = member.deviceId,
            )
        whenever(memberReader.getById(memberId = member.id)).thenReturn(member)
        doNothing().whenever(memberWriter).delete(member.id)
        doNothing().whenever(outboxEventPublisher).publish(EventType.MEMBER_DELETED, payload)

        memberService.delete(member.id)

        verify(memberReader).getById(member.id)
        verify(memberWriter).delete(member.id)
        verify(outboxEventPublisher).publish(EventType.MEMBER_DELETED, payload)
    }
}
