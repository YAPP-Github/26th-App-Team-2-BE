package com.yapp.brake.common.event.payload

import com.yapp.brake.common.event.EventPayload

data class AuthWithdrawEventPayload(
    val userId: Long,
) : EventPayload
