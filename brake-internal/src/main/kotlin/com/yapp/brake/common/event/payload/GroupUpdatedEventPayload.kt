package com.yapp.brake.common.event.payload

import com.yapp.brake.common.event.EventPayload

data class GroupUpdatedEventPayload(
    val deletedAppIds: List<Long>,
) : EventPayload
