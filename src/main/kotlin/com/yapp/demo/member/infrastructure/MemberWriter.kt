package com.yapp.demo.member.infrastructure

import com.yapp.demo.member.model.Member

interface MemberWriter {
    fun save(member: Member): Member
}
