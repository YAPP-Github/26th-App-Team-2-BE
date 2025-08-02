package com.yapp.brake.support.restdocs

import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation

open class Header(
    val descriptor: HeaderDescriptor,
) {
    open infix fun optional(value: Boolean): Header {
        if (value) descriptor.optional()
        return this
    }
}

infix fun String.description(description: String): Header {
    return createHeader(this, description)
}

private fun createHeader(
    value: String,
    description: String,
): Header {
    val descriptor =
        HeaderDocumentation
            .headerWithName(value)
            .description(description)

    return Header(descriptor)
}
