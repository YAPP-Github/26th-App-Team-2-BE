package com.yapp.brake.session.model

import com.yapp.brake.support.fixture.model.sessionFixture
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class SessionTest {
    @Test
    fun `실제 사용 시간을 계산한다`() {
        // given
        val session =
            sessionFixture(
                start = LocalDateTime.of(2025, 7, 18, 12, 0, 0),
                end = LocalDateTime.of(2025, 7, 18, 12, 30, 0),
            )

        // when
        val actual = session.actualTime()

        // then
        assertEquals(1800L, actual)
    }
}
