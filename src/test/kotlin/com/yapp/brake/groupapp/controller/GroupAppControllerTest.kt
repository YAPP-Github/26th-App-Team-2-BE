package com.yapp.brake.groupapp.controller

import andDocument
import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.groupapp.dto.request.AddGroupAppRequest
import com.yapp.brake.groupapp.dto.response.AddGroupAppResponse
import com.yapp.brake.support.RestApiTestBase
import com.yapp.brake.support.restdocs.NUMBER
import com.yapp.brake.support.restdocs.OBJECT
import com.yapp.brake.support.restdocs.STRING
import com.yapp.brake.support.restdocs.Tag
import com.yapp.brake.support.restdocs.means
import com.yapp.brake.support.restdocs.pathParameters
import com.yapp.brake.support.restdocs.toJsonString
import com.yapp.brake.support.restdocs.type
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import requestBody
import responseBody

class GroupAppControllerTest : RestApiTestBase() {
    @Test
    fun `관리 앱 추가 API`() {
        val request = AddGroupAppRequest(groupId = 1L, appId = "kakaoTalk")
        val response = ApiResponse.success(HttpStatus.CREATED.value(), AddGroupAppResponse(groupAppId = 1L))

        whenever(groupAppUseCase.add(request.groupId, request.appId)).thenReturn(response.data)

        val builder =
            RestDocumentationRequestBuilders.post("/v1/group-apps")
                .content(request.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(builder)
            .andExpect(status().isOk)
            .andDocument(
                "group-apps-add",
                Tag.GROUP_APP,
                requestBody(
                    "groupId" type NUMBER means "관리 앱을 추가할 그룹",
                    "appId" type STRING means "관리 앱 식별자(이름 또는 구별할 수 있는 값)",
                ),
                responseBody(
                    "data" type OBJECT means "응답 바디",
                    "data.groupAppId" type NUMBER means "관리 앱 식별자",
                    "code" type NUMBER means "HTTP 코드",
                ),
            )
    }

    @Test
    fun `관리 앱 삭제 API`() {
        val groupAppId = 1L

        doNothing().whenever(groupAppUseCase).remove(groupAppId)

        val builder = RestDocumentationRequestBuilders.delete("/v1/group-apps/{groupAppId}", groupAppId)

        mockMvc.perform(builder)
            .andExpect(status().isNoContent)
            .andDocument(
                "group-apps-remove",
                Tag.GROUP_APP,
                pathParameters("groupAppId" means "제거할 관리 앱 식별자"),
            )
    }
}
