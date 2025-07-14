package com.yapp.brake.group.model

import java.time.LocalDateTime

data class Group(
    val groupId: Long,
    val name: String,
    val createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
) {
    fun update(name: String = this.name) =
        copy(
            name = name,
        )

    companion object {
        fun create(name: String) =
            Group(
                groupId = 0L,
                name = name,
            )
    }
}
