package com.yapp.brake.member.service

import com.yapp.brake.member.dto.response.MemberResponse

interface MemberUseCase {
    fun getMember(memberId: Long): MemberResponse

    fun update(nickname: String): MemberResponse
}
