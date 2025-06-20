package com.yapp.demo.user.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByAuthEmail(authEmail: String): UserEntity?
}
