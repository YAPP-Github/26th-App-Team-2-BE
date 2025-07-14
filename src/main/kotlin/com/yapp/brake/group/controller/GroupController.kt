package com.yapp.brake.group.controller

import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.group.dto.request.CreateGroupRequest
import com.yapp.brake.group.dto.response.CreateGroupResponse
import com.yapp.brake.group.service.GroupUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/groups")
class GroupController(
    private val groupUseCase: GroupUseCase,
) {
    @PostMapping
    fun create(
        @Valid
        request: CreateGroupRequest,
    ): ApiResponse<CreateGroupResponse> {
        return ApiResponse.success(
            code = HttpStatus.CREATED.value(),
            data = groupUseCase.create(request.name),
        )
    }

    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(
        @PathVariable
        groupId: Long,
    )  {
        groupUseCase.remove(groupId)
    }
}
