package com.yapp.demo.user.infrastructure

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
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

    fun getById(userId: Long): User =
        userRepository.findById(userId)
            .getOrNull()
            ?.toDomain()
            ?: throw CustomException(ErrorCode.USER_NOT_FOUND)

    fun findByAuthEmail(email: String): User? = userRepository.findByAuthEmail(email)?.toDomain()
}
