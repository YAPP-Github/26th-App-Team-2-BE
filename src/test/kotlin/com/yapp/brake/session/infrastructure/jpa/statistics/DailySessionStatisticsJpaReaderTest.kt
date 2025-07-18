package com.yapp.brake.session.infrastructure.jpa.statistics

import com.yapp.brake.session.model.DailySessionStatistics
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.util.Optional
import kotlin.test.Test

class DailySessionStatisticsJpaReaderTest {
    private val dailySessionStatisticsRepository = mock<DailySessionStatisticsRepository>()
    private val dailySessionStatisticsReader = DailySessionStatisticsJpaReader(dailySessionStatisticsRepository)

    private val memberId = 1L
    private val date = LocalDate.of(2025, 7, 18)
    private val id = DailySessionStatisticsId(memberId, date)

    @Test
    fun `데이터가 없으면 기본값을 가진 DailySessionStatistics를 반환한다`() {
        // given
        whenever(dailySessionStatisticsRepository.findById(id)).thenReturn(Optional.empty())

        // when
        val result = dailySessionStatisticsReader.getById(memberId, date)

        // then
        assertEquals(DailySessionStatistics(memberId, date), result)
        verify(dailySessionStatisticsRepository).findById(id)
    }
}
