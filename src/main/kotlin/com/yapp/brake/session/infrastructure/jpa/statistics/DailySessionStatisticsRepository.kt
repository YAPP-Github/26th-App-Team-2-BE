package com.yapp.brake.session.infrastructure.jpa.statistics

import org.springframework.data.jpa.repository.JpaRepository

interface DailySessionStatisticsRepository : JpaRepository<DailySessionStatisticsEntity, DailySessionStatisticsId>
