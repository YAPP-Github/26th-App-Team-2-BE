package com.yapp.brake.group.controller

import andDocument
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.support.RestApiTestBase
import com.yapp.brake.support.fixture.dto.group.createGroupRequestFixture
import com.yapp.brake.support.fixture.dto.group.groupResponseFixture
import com.yapp.brake.support.fixture.dto.group.groupsResponseFixture
import com.yapp.brake.support.fixture.dto.group.updateGroupAppRequestFixture
import com.yapp.brake.support.fixture.dto.group.updateGroupRequestFixture
import com.yapp.brake.support.restdocs.ARRAY
import com.yapp.brake.support.restdocs.NUMBER
import com.yapp.brake.support.restdocs.OBJECT
import com.yapp.brake.support.restdocs.STRING
import com.yapp.brake.support.restdocs.Tag
import com.yapp.brake.support.restdocs.means
import com.yapp.brake.support.restdocs.toJsonString
import com.yapp.brake.support.restdocs.type
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GroupControllerTest : RestApiTestBase() {
    @Test
    fun `관리 앱 그룹 생성 API`() {
        val memberId = 1L
        val deviceProfileId = 1L
        val request = createGroupRequestFixture()
        val response =
            ApiResponse.success(
                code = HttpStatus.CREATED.value(),
                data = groupResponseFixture(request),
            )

        whenever(groupUseCase.create(deviceProfileId, request))
            .thenReturn(response.data)

        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), deviceProfileId)
        SecurityContextHolder.getContext().authentication = authentication

        val builder =
            RestDocumentationRequestBuilders.post("/v1/groups")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument("groups-create") {
                tag(Tag.GROUP)
                requestSchema(request::class.java.simpleName)
                responseSchema(response.data!!::class.java.simpleName)
                requestBody(
                    "name" type STRING means "그룹 이름",
                    "groupApps[]" type ARRAY means "관리 앱 목록",
                    "groupApps[].name" type STRING means "관리 앱 이름",
                )
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.groupId" type NUMBER means "그룹 식별자",
                    "data.name" type STRING means "그룹 이름",
                    "data.groupApps[]" type ARRAY means "관리 앱 목록",
                    "data.groupApps[].groupAppId" type NUMBER means "관리 앱 식별자",
                    "data.groupApps[].name" type STRING means "관리 앱 이름",
                    "code" type NUMBER means "HTTP 코드",
                )
            }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `관리 앱 그룹 조회 API`() {
        val memberId = 1L
        val deviceProfileId = 1L
        val response =
            ApiResponse.success(
                code = HttpStatus.OK.value(),
                data = groupsResponseFixture(),
            )

        whenever(groupUseCase.getAll(deviceProfileId))
            .thenReturn(response.data)

        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), deviceProfileId)
        SecurityContextHolder.getContext().authentication = authentication

        val builder =
            RestDocumentationRequestBuilders.get("/v1/groups")

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument("groups-get-all") {
                tag(Tag.GROUP)
                responseSchema(response.data!!::class.java.simpleName)
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.groups[]" type ARRAY means "그룹 리스트",
                    "data.groups[].groupId" type NUMBER means "그룹 식별자",
                    "data.groups[].name" type STRING means "그룹 이름",
                    "data.groups[].groupApps[]" type ARRAY means "관리 앱 목록",
                    "data.groups[].groupApps[].groupAppId" type NUMBER means "관리 앱 식별자",
                    "data.groups[].groupApps[].name" type STRING means "관리 앱 이름",
                    "code" type NUMBER means "HTTP 코드",
                )
            }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `관리 앱 그룹 수정 API`() {
        val memberId = 1L
        val deviceProfileId = 1L
        val groupId = 1L
        val request =
            updateGroupRequestFixture(
                groupApps =
                    listOf(
                        updateGroupAppRequestFixture(1L, "카카오톡"),
                        updateGroupAppRequestFixture(2L, "인스타그램"),
                        updateGroupAppRequestFixture(name = "카카오톡"),
                        updateGroupAppRequestFixture(name = "인스타그램"),
                    ),
            )
        val response =
            ApiResponse.success(
                code = HttpStatus.OK.value(),
                data = groupResponseFixture(request),
            )

        whenever(groupUseCase.modify(deviceProfileId, groupId, request))
            .thenReturn(response.data)

        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), deviceProfileId)
        SecurityContextHolder.getContext().authentication = authentication

        val builder =
            RestDocumentationRequestBuilders.put("/v1/groups/{groupId}", groupId)
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument("groups-update") {
                tag(Tag.GROUP)
                pathParameters("groupId" means "그룹 식별자")
                requestSchema(request::class.java.simpleName)
                responseSchema(response.data!!::class.java.simpleName)
                requestBody(
                    "name" type STRING means "그룹 이름",
                    "groupApps[]" type ARRAY means "관리 앱 목록",
                    "groupApps[].groupAppId" type NUMBER means "관리 앱 식별자" optional true,
                    "groupApps[].name" type STRING means "관리 앱 이름",
                )
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.groupId" type NUMBER means "그룹 식별자",
                    "data.name" type STRING means "그룹 이름",
                    "data.groupApps[]" type ARRAY means "관리 앱 목록",
                    "data.groupApps[].groupAppId" type NUMBER means "관리 앱 식별자",
                    "data.groupApps[].name" type STRING means "관리 앱 이름",
                    "code" type NUMBER means "HTTP 코드",
                )
            }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `관리 앱 그룹 삭제 API`() {
        val memberId = 1L
        val groupId = 1L
        val deviceProfileId = 1L

        doNothing().whenever(groupUseCase).remove(deviceProfileId, groupId)

        val authentication = UsernamePasswordAuthenticationToken(memberId.toString(), deviceProfileId)
        SecurityContextHolder.getContext().authentication = authentication

        val builder = RestDocumentationRequestBuilders.delete("/v1/groups/{groupId}", groupId)

        mockMvc.perform(builder)
            .andExpect(status().isNoContent)
            .andDocument("groups-remove") {
                tag(Tag.GROUP)
                pathParameters("groupId" means "그룹 식별자")
            }

        SecurityContextHolder.clearContext()
    }
}
