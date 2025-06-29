package com.yapp.demo.member.dto.response

import com.yapp.demo.member.model.Member

data class MemberResponse(
    val nickname: String?,
    val status: String,
) {
    companion object {
        fun from(member: Member) =
            MemberResponse(
                nickname = member.nickname,
                status = member.state.name,
            )
    }
}
