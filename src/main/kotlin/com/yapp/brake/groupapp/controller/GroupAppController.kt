package com.yapp.brake.groupapp.controller

import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.groupapp.dto.request.AddGroupAppRequest
import com.yapp.brake.groupapp.dto.response.AddGroupAppResponse
import com.yapp.brake.groupapp.service.GroupAppUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/group-apps")
class GroupAppController(
    private val groupAppUseCase: GroupAppUseCase,
) {
    @PostMapping
    fun add(
        @Valid @RequestBody
        request: AddGroupAppRequest,
    ): ApiResponse<AddGroupAppResponse> =
        ApiResponse.success(
            code = HttpStatus.CREATED.value(),
            data = groupAppUseCase.add(request.groupId, request.appId),
        )

    @DeleteMapping("/{groupAppId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(
        @PathVariable
        groupAppId: Long,
    ) {
        groupAppUseCase.remove(groupAppId)
    }
}
