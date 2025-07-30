package com.yapp.brake.group.infrastructure

import com.yapp.brake.group.model.Group

interface GroupWriter {
    fun save(group: Group): Group

    fun delete(group: Group)
}
