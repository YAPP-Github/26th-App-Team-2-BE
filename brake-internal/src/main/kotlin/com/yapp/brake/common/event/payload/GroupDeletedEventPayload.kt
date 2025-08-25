package com.yapp.brake.common.event.payload

import com.yapp.brake.common.event.EventPayload

data class GroupDeletedEventPayload(
    val groupId: Long,
) : EventPayload
