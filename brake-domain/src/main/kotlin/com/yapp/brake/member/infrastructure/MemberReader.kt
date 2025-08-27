package com.yapp.brake.member.infrastructure

import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.member.model.Member

interface MemberReader {
    fun findByOauthInfo(
        email: String,
        socialProvider: SocialProvider,
    ): Member?

    fun findById(memberId: Long): Member?

    fun getById(memberId: Long): Member
}
