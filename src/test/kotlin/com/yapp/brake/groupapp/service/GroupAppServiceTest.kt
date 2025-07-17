package com.yapp.brake.groupapp.service

import com.yapp.brake.groupapp.infrastructure.GroupAppWriter
import com.yapp.brake.support.fixture.model.groupAppFixture
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GroupAppServiceTest {
    private val groupAppWriter = mock<GroupAppWriter>()
    private val groupAppService = GroupAppService(groupAppWriter)

    @Test
    fun `특정 그룹의 관리앱을 저장 합니다`() {
        val groupApp = groupAppFixture(groupAppId = 0L)

        whenever(groupAppWriter.save(any())).thenReturn(groupApp)

        groupAppService.add(groupApp.groupId, groupApp.name)

        verify(groupAppWriter).save(groupApp)
    }

    @Test
    fun `특정 그룹의 관리앱을 삭제 합니다`() {
        val groupAppId = 1L

        doNothing().whenever(groupAppWriter).delete(groupAppId)

        groupAppService.remove(groupAppId)

        verify(groupAppWriter).delete(groupAppId)
    }
}
