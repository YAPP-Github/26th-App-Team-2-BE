package com.yapp.demo.member.infrastructure

import com.yapp.demo.member.model.Member

interface MemberReader {
    fun findByDeviceId(deviceId: String): Member?

    fun findById(memberId: Long): Member?

    fun getById(memberId: Long): Member
}
