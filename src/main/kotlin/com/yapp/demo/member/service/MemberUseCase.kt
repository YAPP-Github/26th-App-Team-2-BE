package com.yapp.demo.member.service

import com.yapp.demo.member.dto.response.MemberResponse

interface MemberUseCase {
    fun getMember(memberId: Long): MemberResponse

    fun update(nickname: String): MemberResponse

    fun remove(memberId: Long)
}
