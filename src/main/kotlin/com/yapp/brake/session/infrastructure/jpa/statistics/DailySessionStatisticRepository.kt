package com.yapp.brake.session.infrastructure.jpa.statistics

import org.springframework.data.jpa.repository.JpaRepository

interface DailySessionStatisticRepository : JpaRepository<DailySessionStatisticEntity, DailySessionStatisticId>
