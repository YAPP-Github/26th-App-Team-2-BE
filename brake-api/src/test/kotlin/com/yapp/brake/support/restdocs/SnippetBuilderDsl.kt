package com.yapp.brake.support.restdocs

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema

class SnippetBuilderDsl {
    private val builder = ResourceSnippetParameters.builder()

    fun tag(tag: Tag) {
        builder.tag(tag.name)
    }

    fun queryParameters(vararg params: Param) {
        builder.queryParameters(*params.map { it.descriptor }.toTypedArray())
    }

    fun pathParameters(vararg params: Param) {
        builder.pathParameters(*params.map { it.descriptor }.toTypedArray())
    }

    fun requestBody(vararg fields: Field) {
        builder.requestFields(fields.map { it.descriptor })
    }

    fun requestSchema(requestDtoClassName: String) {
        builder.requestSchema(Schema(requestDtoClassName))
    }

    fun responseBody(vararg fields: Field) {
        builder.responseFields(fields.map { it.descriptor })
    }

    fun responseSchema(responseDtoClassName: String) {
        builder.responseSchema(Schema(responseDtoClassName))
    }

    fun requestHeaders(vararg headers: Header) {
        builder.requestHeaders(*headers.map { it.descriptor }.toTypedArray())
    }

    fun build(): ResourceSnippetParameters = builder.build()
}
