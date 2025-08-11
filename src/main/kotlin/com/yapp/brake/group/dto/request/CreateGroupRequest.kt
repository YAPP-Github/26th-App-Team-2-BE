package com.yapp.brake.group.dto.request

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.common.serializer.DataSerializer.deserialize
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateGroupRequest(
    @field:NotBlank
    @field:Size(min = 2, max = 10)
    val name: String,
    @field:Valid
    val groupApps: List<AddGroupAppRequest> = emptyList(),
) {
    companion object {
        fun from(rawRequest: CreateGroupIosRequest): CreateGroupRequest {
            val groupApps =
                deserialize<List<AddGroupAppRequest>>(rawRequest.groupApps)
                    ?: throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)

            return CreateGroupRequest(
                name = rawRequest.name,
                groupApps = groupApps,
            )
        }
    }
}
