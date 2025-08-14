package com.yapp.brake.controller

import com.yapp.brake.common.dto.ApiResponse
import com.yapp.brake.common.security.getMemberId
import com.yapp.brake.member.dto.request.UpdateNicknameRequest
import com.yapp.brake.member.dto.response.MemberResponse
import com.yapp.brake.member.service.MemberUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/members")
class MemberController(
    private val memberUseCase: MemberUseCase,
) {
    @GetMapping("/me")
    fun getMyInfo(): ApiResponse<MemberResponse> = ApiResponse.success(memberUseCase.getMember(getMemberId()))

    @PatchMapping("/me")
    fun update(
        @Valid @RequestBody
        request: UpdateNicknameRequest,
    ): ApiResponse<MemberResponse> = ApiResponse.success(memberUseCase.update(getMemberId(), request.nickname))

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete() {
        memberUseCase.delete(getMemberId())
    }
}
