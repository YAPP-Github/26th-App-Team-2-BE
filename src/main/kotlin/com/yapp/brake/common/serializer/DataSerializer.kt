package com.yapp.brake.common.serializer

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger { }

object DataSerializer {
    private val objectMapper: ObjectMapper = initialize()

    private fun initialize(): ObjectMapper = jacksonObjectMapper().registerModules(JavaTimeModule())

    fun <T> deserialize(
        data: String?,
        clazz: Class<T>,
    ): T? =
        try {
            objectMapper.readValue(data, clazz)
        } catch (e: JsonProcessingException) {
            logger.error(e) { "[DataSerializer.deserialize] data=$data, clazz=$clazz" }
            null
        }

    fun <T> deserialize(
        data: Any?,
        clazz: Class<T>,
    ): T = objectMapper.convertValue(data, clazz)

    fun serialize(any: Any?): String? =
        try {
            objectMapper.writeValueAsString(any)
        } catch (e: JsonProcessingException) {
            logger.error(e) { "[DataSerializer.serialize] object=$any" }
            null
        }
}
