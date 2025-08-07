package com.yapp.brake.session.infrastructure.jpa

import com.yapp.brake.session.infrastructure.SessionWriter
import com.yapp.brake.session.model.Session
import org.springframework.stereotype.Repository

@Repository
class SessionJpaWriter(
    private val sessionRepository: SessionRepository,
) : SessionWriter {
    override fun save(session: Session): Session {
        val sessionEntity = SessionEntity.create(session)
        return sessionRepository.save(sessionEntity).toDomain()
    }
}
