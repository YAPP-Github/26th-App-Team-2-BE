package com.yapp.demo.member.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<MemberEntity, Long> {
    fun findByAuthEmail(authEmail: String): MemberEntity?
}
