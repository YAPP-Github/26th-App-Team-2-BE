package com.yapp.brake.session.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository : JpaRepository<SessionEntity, Long>
