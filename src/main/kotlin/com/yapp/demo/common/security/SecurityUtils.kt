package com.yapp.demo.common.security

import org.springframework.security.core.context.SecurityContextHolder

fun getUserId(): Long = SecurityContextHolder.getContext().authentication.name.toLong()
