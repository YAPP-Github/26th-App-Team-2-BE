package com.yapp.brake.group.dto.request

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.common.serializer.DataSerializer.deserialize
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateGroupRequest(
    @field:NotBlank
    @field:Size(min = 2, max = 10)
    val name: String,
    @field:Valid
    val groupApps: List<UpdateGroupAppRequest> = emptyList(),
) {
    companion object {
        fun from(rawRequest: UpdateGroupIosRequest): UpdateGroupRequest {
            val groupApps =
                deserialize<List<UpdateGroupAppRequest>>(rawRequest.groupApps)
                    ?: throw CustomException(ErrorCode.BAD_REQUEST)

            return UpdateGroupRequest(rawRequest.name, groupApps)
        }
    }
}
