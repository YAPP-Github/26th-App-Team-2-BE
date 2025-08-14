package com.yapp.brake.controller

import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.common.security.getDeviceProfileId
import com.yapp.brake.group.dto.request.CreateGroupRequest
import com.yapp.brake.group.dto.request.UpdateGroupRequest
import com.yapp.brake.group.dto.response.GroupResponse
import com.yapp.brake.group.dto.response.GroupsResponse
import com.yapp.brake.group.service.GroupUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
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
        @Valid @RequestBody
        request: CreateGroupRequest,
    ): ApiResponse<GroupResponse> {
        return ApiResponse.success(
            status = HttpStatus.CREATED.value(),
            data = groupUseCase.create(getDeviceProfileId(), request),
        )
    }

    @GetMapping
    fun readAll(): ApiResponse<GroupsResponse> = ApiResponse.success(groupUseCase.getAll(getDeviceProfileId()))

    @PutMapping("/{groupId}")
    fun modify(
        @PathVariable @Positive
        groupId: Long,
        @Valid @RequestBody
        request: UpdateGroupRequest,
    ): ApiResponse<GroupResponse> {
        return ApiResponse.success(groupUseCase.modify(getDeviceProfileId(), groupId, request))
    }

    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(
        @PathVariable @Positive
        groupId: Long,
    ) {
        groupUseCase.remove(getDeviceProfileId(), groupId)
    }
}
