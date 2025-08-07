package com.yapp.brake.session.infrastructure

import com.yapp.brake.session.model.Session

interface SessionWriter {
    fun save(session: Session): Session
}
