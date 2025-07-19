package com.yapp.brake.session.model

import java.time.LocalDate

data class Sessions(
    val sessions: List<Session>,
) {
    fun getByDate(date: LocalDate): Session? {
        return sessions.firstOrNull { it.start.toLocalDate() == date }
    }
//
//    fun update(sessionStatistics: SessionStatistics) {
//        sessions.forEach { session ->
//            sessionStatistics
//                .getByDate(session.start.toLocalDate())
//                .add(session)
//        }
//    }
}
