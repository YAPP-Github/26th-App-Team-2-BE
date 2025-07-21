package com.yapp.brake.statistic.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface DailySessionStatisticRepository : JpaRepository<DailySessionStatisticEntity, DailySessionStatisticId>
