package com.yapp.brake.group.service

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.group.dto.response.GroupResponse
import com.yapp.brake.group.infrastructure.GroupReader
import com.yapp.brake.group.infrastructure.GroupWriter
import com.yapp.brake.group.model.Group
import com.yapp.brake.groupapp.infrastructure.GroupAppReader
import com.yapp.brake.groupapp.infrastructure.GroupAppWriter
import com.yapp.brake.groupapp.model.GroupApp
import com.yapp.brake.outbox.infrastructure.event.OutboxEventPublisher
import com.yapp.brake.support.fixture.dto.group.createGroupRequestFixture
import com.yapp.brake.support.fixture.dto.group.updateGroupAppRequestFixture
import com.yapp.brake.support.fixture.dto.group.updateGroupRequestFixture
import com.yapp.brake.support.fixture.model.groupAppFixture
import com.yapp.brake.support.fixture.model.groupFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GroupServiceTest {
    private val groupWriter = mock<GroupWriter>()
    private val groupReader = mock<GroupReader>()
    private val groupAppReader = mock<GroupAppReader>()
    private val groupAppWriter = mock<GroupAppWriter>()
    private val outboxEventPublisher = mock<OutboxEventPublisher>()
    private val groupService =
        GroupService(groupWriter, groupReader, groupAppWriter, groupAppReader, outboxEventPublisher)

    @Test
    fun `관리 앱 그룹을 생성한다`() {
        val request = createGroupRequestFixture()
        val group = groupFixture(groupId = 1L, name = request.name)
        val groupApps =
            request.groupApps.mapIndexed { idx, it ->
                groupAppFixture(
                    groupAppId = idx + 1L,
                    groupId = group.groupId,
                    name = it.name,
                )
            }

        whenever(groupWriter.save(any())).thenReturn(group)
        whenever(groupAppWriter.save(any())).thenAnswer { invocation ->
            val arg = invocation.arguments[0] as GroupApp
            groupApps.first { it.name == arg.name }
        }

        val result = groupService.create(group.deviceProfileId, request)

        assertThat(result.groupId).isEqualTo(group.groupId)
        assertThat(result.name).isEqualTo(group.name)
        assertThat(result.groupApps).hasSize(groupApps.size)

        verify(groupWriter).save(any())
        verify(groupAppWriter, times(groupApps.size)).save(any())
    }

    @Test
    fun `디바이스 프로필의 관리 그룹을 모두 조회한다`() {
        val deviceProfileId = 1L
        val groups =
            List(3) { i ->
                groupFixture(groupId = (i + 1).toLong(), deviceProfileId = deviceProfileId, name = "SNS$i")
            }

        val groupApps =
            groups.mapIndexed { index, g ->
                groupAppFixture(
                    groupId = g.groupId,
                    groupAppId = (index + 1).toLong(),
                    name = "앱$index",
                )
            }

        whenever(groupReader.getAllByDeviceProfileId(deviceProfileId)).thenReturn(groups)
        whenever(groupAppReader.getByGroupIds(any())).thenReturn(groupApps)

        val result = groupService.getAll(deviceProfileId)

        assertThat(
            result.groups.map(GroupResponse::groupId),
        ).containsExactlyInAnyOrderElementsOf(groups.map(Group::groupId))
        assertThat(result.groups.flatMap { it.groupApps }.map { it.name })
            .containsExactlyInAnyOrderElementsOf(groupApps.map { it.name })

        verify(groupReader).getAllByDeviceProfileId(deviceProfileId)
        verify(groupAppReader).getByGroupIds(any())
    }

    @Nested
    inner class ModifyTest {
        @Test
        fun `관리 앱 그룹을 수정한다 (관리 앱 변화 X)`() {
            val request = updateGroupRequestFixture()
            val group = groupFixture()
            val updatedGroup = groupFixture(groupId = group.groupId, name = request.name)
            val groupApps =
                request.groupApps.mapIndexed { idx, it ->
                    groupAppFixture(
                        groupAppId = idx + 1L,
                        groupId = group.groupId,
                        name = it.name,
                    )
                }

            whenever(groupReader.getByIdAndDeviceProfileId(group.groupId, group.deviceProfileId)).thenReturn(group)
            whenever(groupWriter.save(any())).thenReturn(updatedGroup)
            whenever(groupAppReader.getByGroupId(group.groupId)).thenReturn(groupApps)

            val result = groupService.modify(group.deviceProfileId, group.groupId, request)

            assertThat(result.groupId).isEqualTo(group.groupId)
            assertThat(result.name).isEqualTo(group.name)

            verify(groupReader).getByIdAndDeviceProfileId(group.groupId, group.deviceProfileId)
            verify(groupWriter).save(any())
            verify(outboxEventPublisher, never()).publish(any(), any())
        }

        @Test
        fun `관리 앱 그룹을 수정한다 (관리 앱 새로 추가 및 삭제)`() {
            val originGroupApps =
                listOf(
                    groupAppFixture(groupAppId = 1L, name = "카카오톡"),
                    groupAppFixture(groupAppId = 2L, name = "인스타그램"),
                )

            val request =
                updateGroupRequestFixture(
                    groupApps =
                        listOf(
                            updateGroupAppRequestFixture(0L, "페이스북"),
                            updateGroupAppRequestFixture(0L, "트위터"),
                            updateGroupAppRequestFixture(originGroupApps[0].groupAppId, originGroupApps[0].name),
                        ),
                )

            val group = groupFixture()
            val updatedGroup = groupFixture(groupId = group.groupId, name = request.name)

            val updatedGroupApps =
                request.groupApps.mapIndexed { idx, it ->
                    groupAppFixture(
                        groupAppId = idx + 100L,
                        groupId = group.groupId,
                        name = it.name,
                    )
                }

            whenever(groupReader.getByIdAndDeviceProfileId(group.groupId, group.deviceProfileId)).thenReturn(group)
            whenever(groupWriter.save(any())).thenReturn(updatedGroup)
            whenever(groupAppReader.getByGroupId(group.groupId)).thenReturn(originGroupApps)
            whenever(groupAppWriter.save(any())).thenAnswer { invocation ->
                val arg = invocation.arguments[0] as GroupApp
                updatedGroupApps.first { it.name == arg.name }
            }

            val result = groupService.modify(group.deviceProfileId, group.groupId, request)

            assertThat(result.groupId).isEqualTo(group.groupId)
            assertThat(result.name).isEqualTo(group.name)
            assertThat(result.groupApps).hasSize(request.groupApps.size)

            verify(groupAppWriter, times(2)).save(any())
            verify(groupReader).getByIdAndDeviceProfileId(group.groupId, group.deviceProfileId)
            verify(groupWriter).save(any())
            verify(outboxEventPublisher).publish(any(), any())
        }

        @Test
        fun `관리 앱 그룹이 존재하지 않으면 예외를 던진다`() {
            val invalidMemberId = 12345L
            val invalidGroupId = 12345L

            whenever(groupReader.getByIdAndDeviceProfileId(invalidGroupId, invalidMemberId))
                .thenThrow(CustomException(ErrorCode.GROUP_NOT_FOUND))

            val result =
                assertThrows<CustomException> {
                    groupService.modify(invalidMemberId, invalidGroupId, updateGroupRequestFixture())
                }

            assertThat(result.errorCode).isEqualTo(ErrorCode.GROUP_NOT_FOUND)

            verify(groupReader).getByIdAndDeviceProfileId(invalidGroupId, invalidMemberId)
            verify(groupWriter, never()).save(any())
            verify(groupAppReader, never()).getByGroupId(any())
            verify(outboxEventPublisher, never()).publish(any(), any())
        }
    }

    @Nested
    inner class RemoveTest {
        @Test
        fun `관리 그룹을 삭제한다`() {
            val group = groupFixture(groupId = 1L)

            whenever(groupReader.getByIdAndDeviceProfileId(group.groupId, group.deviceProfileId)).thenReturn(group)
            doNothing().whenever(outboxEventPublisher).publish(any(), any())

            groupService.remove(group.deviceProfileId, group.groupId)

            verify(groupReader).getByIdAndDeviceProfileId(group.groupId, group.deviceProfileId)
            verify(groupWriter).delete(group)
            verify(outboxEventPublisher).publish(any(), any())
        }

        @Test
        fun `관리 앱 그룹이 존재하지 않으면 예외를 던진다`() {
            val invalidMemberId = 12345L
            val invalidGroupId = 12345L

            whenever(groupReader.getByIdAndDeviceProfileId(invalidMemberId, invalidGroupId))
                .thenThrow(CustomException(ErrorCode.GROUP_NOT_FOUND))

            val result =
                assertThrows<CustomException> {
                    groupService.remove(invalidMemberId, invalidGroupId)
                }

            assertThat(result.errorCode).isEqualTo(ErrorCode.GROUP_NOT_FOUND)

            verify(groupReader).getByIdAndDeviceProfileId(invalidGroupId, invalidMemberId)
            verify(groupWriter, never()).delete(any())
            verify(outboxEventPublisher, never()).publish(any(), any())
        }
    }
}
