package com.yapp.demo.user.infrastructure

import com.yapp.demo.user.infrastructure.jpa.UserEntity
import com.yapp.demo.user.infrastructure.jpa.UserRepository
import com.yapp.demo.user.model.User
import org.springframework.stereotype.Repository

@Repository
class UserWriter(
    private val userRepository: UserRepository,
) {
    fun save(userEntity: UserEntity): User = userRepository.save(userEntity).toDomain()
}
