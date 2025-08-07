package com.yapp.brake.member.infrastructure

import com.yapp.brake.member.model.Member

interface MemberWriter {
    fun save(member: Member): Member

    fun delete(memberId: Long)
}
