package com.yapp.brake.member.infrastructure

import com.yapp.brake.member.model.Member

interface MemberReader {
    fun findByDeviceId(deviceId: String): Member?

    fun findById(memberId: Long): Member?

    fun getById(memberId: Long): Member
}
