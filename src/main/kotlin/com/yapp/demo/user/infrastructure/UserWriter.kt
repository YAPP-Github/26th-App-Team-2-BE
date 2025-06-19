package com.yapp.demo.user.infrastructure

import com.yapp.demo.user.infrastructure.jpa.UserEntity
import com.yapp.demo.user.infrastructure.jpa.UserRepository
import com.yapp.demo.user.model.User
import org.springframework.stereotype.Repository

@Repository
class UserWriter(
    private val userRepository: UserRepository,
) {
    fun save(user: User): User {
        val entity = UserEntity.from(user)
        return userRepository.save(entity).toDomain()
    }
}
