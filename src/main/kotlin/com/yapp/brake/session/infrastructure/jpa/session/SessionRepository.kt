package com.yapp.brake.session.infrastructure.jpa.session

import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository : JpaRepository<SessionEntity, Long>
