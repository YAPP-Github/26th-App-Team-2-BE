package com.yapp.demo.member.controller

import com.yapp.demo.common.dto.ApiResponse
import com.yapp.demo.common.security.getMemberId
import com.yapp.demo.member.dto.request.UpdateNicknameRequest
import com.yapp.demo.member.dto.response.MemberResponse
import com.yapp.demo.member.service.MemberUseCase
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
        @RequestBody
        request: UpdateNicknameRequest,
    ): ApiResponse<MemberResponse> = ApiResponse.success(memberUseCase.update(request.nickname))

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun withdraw() {
        memberUseCase.remove(getMemberId())
    }
}
