package com.yapp.brake.session.model

data class Snooze(
    val count: Int,
    val unit: Int,
) {
    companion object {
        fun empty() = Snooze(0, 0)
    }
}
