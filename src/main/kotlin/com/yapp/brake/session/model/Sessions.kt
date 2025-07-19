package com.yapp.brake.session.model

import java.time.LocalDate

data class Sessions(
    val sessions: List<Session>,
) {
    fun getByDate(date: LocalDate): Session? {
        return sessions.firstOrNull { it.start.toLocalDate() == date }
    }
}
