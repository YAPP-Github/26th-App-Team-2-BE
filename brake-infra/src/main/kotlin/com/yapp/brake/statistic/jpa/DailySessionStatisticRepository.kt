package com.yapp.brake.statistic.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface DailySessionStatisticRepository : JpaRepository<DailySessionStatisticEntity, DailySessionStatisticId>
