package com.yapp.brake.support.restdocs

import RestDocsUtils
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation

open class Field(
    // 필드 설명을 담고 있는 FieldDescriptor 객체
    val descriptor: FieldDescriptor,
) {
    /**
     * 'default' 값을 가져오거나 설정합니다.
     *
     * @return 'default' 값
     * @param value 설정할 'default' 값
     */
    protected open var default: String
        get() = descriptor.attributes.getOrDefault(RestDocsAttributeKeys.KEY_DEFAULT, "") as String
        set(value) {
            descriptor.attributes(RestDocsUtils.defaultValue(value))
        }

    /**
     * 'format' 값을 가져오거나 설정합니다.
     *
     * @return 'format' 값
     * @param value 설정할 'format' 값
     */
    protected open var format: String
        get() = descriptor.attributes.getOrDefault(RestDocsAttributeKeys.KEY_FORMAT, "") as String
        set(value) {
            descriptor.attributes(RestDocsUtils.customFormat(value))
        }

    /**
     * 'example' 값을 가져오거나 설정합니다.
     *
     * @return 'example' 값
     * @param value 설정할 'example' 값
     */
    protected open var example: String
        get() = descriptor.attributes.getOrDefault(RestDocsAttributeKeys.KEY_EXAMPLE, "") as String
        set(value) {
            descriptor.attributes(RestDocsUtils.customExample(value))
        }

    /**
     * 필드에 대한 설명을 설정합니다.
     *
     * @param value 설명할 내용
     * @return 설정된 필드 객체
     */
    open infix fun means(value: String): Field {
        descriptor.description(value)
        return this
    }

    /**
     * 필드의 속성을 설정하는 블록을 실행합니다.
     *
     * @param block 필드 속성을 설정하는 람다 블록
     * @return 속성 설정 후의 필드 객체
     */
    open infix fun attributes(block: Field.() -> Unit): Field {
        block()
        return this
    }

    /**
     * 필드의 'default' 값을 설정합니다.
     *
     * @param value 설정할 'default' 값
     * @return 'default' 값이 설정된 필드 객체
     */
    open infix fun withDefaultValue(value: String): Field {
        this.default = value
        return this
    }

    /**
     * 필드의 'format' 값을 설정합니다.
     *
     * @param value 설정할 'format' 값
     * @return 'format' 값이 설정된 필드 객체
     */
    open infix fun formattedAs(value: String): Field {
        this.format = value
        return this
    }

    /**
     * 필드의 'example' 값을 설정합니다.
     *
     * @param value 설정할 'example' 값
     * @return 'example' 값이 설정된 필드 객체
     */
    open infix fun example(value: String): Field {
        this.example = value
        return this
    }

    /**
     * 필드를 선택 사항으로 설정합니다.
     *
     * @param value true이면 필드를 선택 사항으로 설정
     * @return 선택 사항이 설정된 필드 객체
     */
    open infix fun optional(value: Boolean): Field {
        if (value) descriptor.optional()
        return this
    }

    /**
     * 필드를 무시하도록 설정합니다.
     *
     * @param value true이면 필드를 무시하도록 설정
     * @return 무시 설정된 필드 객체
     */
    open infix fun isIgnored(value: Boolean): Field {
        if (value) descriptor.ignored()
        return this
    }
}

/**
 * 필드의 타입을 설정합니다.
 *
 * @param docsFieldType 필드 타입을 지정하는 FieldType 객체
 * @return 타입이 설정된 Field 객체
 */
infix fun String.type(docsFieldType: FieldType): Field {
    return createField(this, docsFieldType.type)
}

/**
 * 주어진 값과 타입으로 필드를 생성합니다.
 *
 * @param value 필드의 경로 또는 이름을 나타내는 문자열
 * @param type 필드의 타입을 나타내는 JsonFieldType 객체
 * @param optional 선택 사항으로 필드를 설정할지 여부 (기본값: true)
 * @return 생성된 Field 객체
 */
private fun createField(
    value: String,
    type: JsonFieldType,
): Field {
    val descriptor =
        PayloadDocumentation.fieldWithPath(value)
            .type(type)
            .description("")

    return Field(descriptor)
}
