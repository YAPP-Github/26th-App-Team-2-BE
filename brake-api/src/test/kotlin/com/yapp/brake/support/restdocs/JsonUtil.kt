package com.yapp.brake.support.restdocs

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

private val objectMapper = jacksonObjectMapper()

// expectedResponse를 JSON 문자열로 변환하는 확장 함수
fun Any.toJson(): ResultMatcher {
    return content().json(objectMapper.writeValueAsString(this))
}

fun Any.toJsonString(): String =
    jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .writeValueAsString(this)
