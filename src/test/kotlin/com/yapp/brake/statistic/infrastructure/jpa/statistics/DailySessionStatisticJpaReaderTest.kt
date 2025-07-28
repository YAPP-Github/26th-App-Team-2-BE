package com.yapp.brake.statistic.infrastructure.jpa.statistics

import com.yapp.brake.statistic.infrastructure.jpa.DailySessionStatisticEntity
import com.yapp.brake.statistic.infrastructure.jpa.DailySessionStatisticId
import com.yapp.brake.statistic.infrastructure.jpa.DailySessionStatisticJpaReader
import com.yapp.brake.statistic.infrastructure.jpa.DailySessionStatisticRepository
import com.yapp.brake.statistic.model.DailySessionStatistic
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.util.Optional
import kotlin.test.Test

class DailySessionStatisticJpaReaderTest {
    private val dailySessionStatisticRepository = mock<DailySessionStatisticRepository>()
    private val dailySessionStatisticsReader = DailySessionStatisticJpaReader(dailySessionStatisticRepository)

    private val memberId = 1L
    private val date = LocalDate.of(2025, 7, 18)
    private val id = DailySessionStatisticId(memberId, date)

    @Test
    fun `데이터가 없으면 기본값을 가진 DailySessionStatistics를 반환한다`() {
        // given
        whenever(dailySessionStatisticRepository.findById(id)).thenReturn(Optional.empty())

        // when
        val result = dailySessionStatisticsReader.getById(memberId, date)

        // then
        assertEquals(DailySessionStatistic(memberId, date), result)
        verify(dailySessionStatisticRepository).findById(id)
    }

    @Test
    fun `데이터가 존재하면 해당 DailySessionStatistics를 반환한다`() {
        // given
        val entity =
            DailySessionStatisticEntity(
                date = date,
                deviceProfileId = memberId,
                actualMinutes = 25L,
                goalMinutes = 30L,
            )
        whenever(dailySessionStatisticRepository.findById(id)).thenReturn(Optional.of(entity))

        // when
        val result = dailySessionStatisticsReader.getById(memberId, date)

        // then
        assertEquals(entity.toDomain(), result)
        verify(dailySessionStatisticRepository).findById(id)
    }
}
