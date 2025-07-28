package com.yapp.brake.member.infrastructure.jpa

import com.yapp.brake.common.enums.SocialProvider
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<MemberEntity, Long> {
    fun findByAuthEmailAndSocialProvider(
        authEmail: String,
        socialProvider: SocialProvider,
    ): MemberEntity?
}
