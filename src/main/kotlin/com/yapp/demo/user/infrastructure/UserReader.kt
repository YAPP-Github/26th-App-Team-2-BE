package com.yapp.demo.user.infrastructure

import com.yapp.demo.user.infrastructure.jpa.UserRepository
import com.yapp.demo.user.model.User
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class UserReader(
    private val userRepository: UserRepository,
) {
    fun findById(userId: Long): User? =
        userRepository.findById(userId)
            .getOrNull()
            ?.toDomain()

    fun findByAuthEmail(email: String): User? = userRepository.findByAuthEmail(email)?.toDomain()
}
