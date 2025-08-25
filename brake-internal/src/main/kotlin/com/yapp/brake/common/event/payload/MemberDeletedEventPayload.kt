package com.yapp.brake.common.event.payload

import com.yapp.brake.common.event.EventPayload

data class MemberDeletedEventPayload(
    val memberId: Long,
    val socialProvider: String,
    val authId: String,
    val authEmail: String,
) : EventPayload
