package com.yapp.brake.member.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<MemberEntity, Long> {
    fun findByDeviceId(deviceId: String): MemberEntity?
}
