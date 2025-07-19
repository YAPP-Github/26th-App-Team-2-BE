package com.yapp.brake.session.model

import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals

class SessionsTest {
    private val session1 =
        sessionFixture(
            start = LocalDateTime.of(2025, 7, 18, 23, 50),
            end = LocalDateTime.of(2025, 7, 18, 23, 59),
        )
    private val session2 =
        sessionFixture(
            start = LocalDateTime.of(2025, 7, 19, 0, 0),
            end = LocalDateTime.of(2025, 7, 19, 0, 20),
        )
    private val sessionList =
        listOf(
            session1,
            session2,
        )

    @Test
    fun `특정 날짜의 세션을 반환한다`() {
        val sessions = Sessions(sessionList)

        // when
        val result = sessions.getByDate(LocalDate.of(2025, 7, 18))

        // then
        assertEquals(result, session1)
    }

    @Test
    fun `특정 날짜의 세션이 없다면 null을 반환한다`() {
        val sessions = Sessions(sessionList)

        // when
        val result = sessions.getByDate(LocalDate.of(2025, 7, 17))

        // then
        assertNull(result)
    }
}
