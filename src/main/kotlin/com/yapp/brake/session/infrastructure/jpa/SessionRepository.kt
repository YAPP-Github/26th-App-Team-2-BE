package com.yapp.brake.session.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository : JpaRepository<SessionEntity, Long>
