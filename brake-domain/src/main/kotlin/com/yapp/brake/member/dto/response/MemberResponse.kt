package com.yapp.brake.member.dto.response

import com.yapp.brake.member.model.Member

data class MemberResponse(
    val nickname: String?,
    val state: String,
) {
    companion object {
        fun from(member: Member) =
            MemberResponse(
                nickname = member.nickname,
                state = member.state.name,
            )
    }
}
