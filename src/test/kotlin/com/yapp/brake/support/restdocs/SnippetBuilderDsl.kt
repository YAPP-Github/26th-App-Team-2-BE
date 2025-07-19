package com.yapp.brake.support.restdocs

import com.epages.restdocs.apispec.ResourceSnippetParameters

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

    fun responseBody(vararg fields: Field) {
        builder.responseFields(fields.map { it.descriptor })
    }

    fun build(): ResourceSnippetParameters = builder.build()
}
