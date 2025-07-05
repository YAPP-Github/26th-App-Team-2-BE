package com.yapp.demo.member.service

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.member.infrastructure.MemberReader
import com.yapp.demo.member.infrastructure.MemberWriter
import com.yapp.demo.member.model.MemberState
import com.yapp.demo.support.fixture.model.memberFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class MemberServiceTest {
    private val memberReader = mock<MemberReader>()
    private val memberWriter = mock<MemberWriter>()
    private val memberService = MemberService(memberReader, memberWriter)

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
}
