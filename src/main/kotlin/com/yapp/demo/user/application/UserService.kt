package com.yapp.demo.user.application

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import com.yapp.demo.user.infrastructure.UserReader
import com.yapp.demo.user.infrastructure.UserWriter
import com.yapp.demo.user.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userWriter: UserWriter,
    private val userReader: UserReader,
) {
    @Transactional(readOnly = true)
    fun loadUserByEmail(email: String): User =
        userReader.findByAuthEmail(email)
            ?: throw CustomException(ErrorCode.USER_NOT_FOUND)
}
