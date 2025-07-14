package com.yapp.brake.group.service

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.group.infrastructure.GroupReader
import com.yapp.brake.group.infrastructure.GroupWriter
import com.yapp.brake.support.fixture.model.groupFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GroupServiceTest {
    private val groupWriter = mock<GroupWriter>()
    private val groupReader = mock<GroupReader>()
    private val groupService = GroupService(groupWriter, groupReader)

    @Test
    fun `관리 앱 그룹을 생성한다`() {
        val expected = groupFixture(groupId = 1L)

        whenever(groupWriter.save(any())).thenReturn(expected)

        val result = groupService.create(expected.memberId, expected.name)

        assertThat(result.groupId).isEqualTo(expected.groupId)
        assertThat(result.name).isEqualTo(expected.name)

        verify(groupWriter).save(any())
    }

    @Nested
    inner class ModifyTest {
        @Test
        fun `관리 앱 그룹을 수정한다`() {
            val group = groupFixture(groupId = 1L)
            val expected = groupFixture(groupId = group.groupId, name = "UPDATE")

            whenever(groupReader.getByIdAndMemberId(group.groupId, group.memberId)).thenReturn(group)
            whenever(groupWriter.save(any())).thenReturn(expected)

            val result = groupService.modify(expected.memberId, expected.groupId, expected.name)

            assertThat(result.groupId).isEqualTo(expected.groupId)
            assertThat(result.name).isEqualTo(expected.name)

            verify(groupReader).getByIdAndMemberId(group.groupId, group.memberId)
            verify(groupWriter).save(any())
        }

        @Test
        fun `관리 앱 그룹이 존재하지 않으면 예외를 던진다`() {
            val invalidMemberId = 12345L
            val invalidGroupId = 12345L

            whenever(groupReader.getByIdAndMemberId(invalidGroupId, invalidMemberId))
                .thenThrow(CustomException(ErrorCode.GROUP_NOT_FOUND))

            val result =
                assertThrows<CustomException> {
                    groupService.modify(invalidMemberId, invalidGroupId, "invalid")
                }

            assertThat(result.errorCode).isEqualTo(ErrorCode.GROUP_NOT_FOUND)

            verify(groupReader).getByIdAndMemberId(invalidGroupId, invalidMemberId)
            verify(groupWriter, never()).save(any())
        }
    }

    @Nested
    inner class RemoveTest {
        @Test
        fun `관리 앱 그룹 삭제한다`() {
            val group = groupFixture(groupId = 1L)

            whenever(groupReader.getByIdAndMemberId(group.groupId, group.memberId)).thenReturn(group)
            doNothing().whenever(groupWriter).delete(group)

            groupService.remove(group.memberId, group.groupId)

            verify(groupReader).getByIdAndMemberId(group.groupId, group.memberId)
            verify(groupWriter).delete(group)
        }

        @Test
        fun `멤버의 관리앱이 존재하지 않으면 예외를 던진다`() {
            val group = groupFixture(groupId = 1L)
            val expected = groupFixture(groupId = group.groupId, name = "UPDATE")

            whenever(groupReader.getByIdAndMemberId(group.groupId, group.memberId)).thenReturn(group)
            whenever(groupWriter.save(any())).thenReturn(expected)

            val result = groupService.modify(expected.memberId, expected.groupId, expected.name)

            assertThat(result.groupId).isEqualTo(expected.groupId)
            assertThat(result.name).isEqualTo(expected.name)

            verify(groupReader).getByIdAndMemberId(group.groupId, group.memberId)
            verify(groupWriter).save(any())
        }

        @Test
        fun `관리 앱 그룹이 존재하지 않으면 예외를 던진다`() {
            val invalidMemberId = 12345L
            val invalidGroupId = 12345L

            whenever(groupReader.getByIdAndMemberId(invalidGroupId, invalidMemberId))
                .thenThrow(CustomException(ErrorCode.GROUP_NOT_FOUND))

            val result =
                assertThrows<CustomException> {
                    groupService.modify(invalidMemberId, invalidGroupId, "invalid")
                }

            assertThat(result.errorCode).isEqualTo(ErrorCode.GROUP_NOT_FOUND)

            verify(groupReader).getByIdAndMemberId(invalidGroupId, invalidMemberId)
            verify(groupWriter, never()).save(any())
        }
    }
}
