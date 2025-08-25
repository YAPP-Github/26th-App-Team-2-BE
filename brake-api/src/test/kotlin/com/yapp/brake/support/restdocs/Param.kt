package com.yapp.brake.support.restdocs

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation

/**
 * 경로 또는 쿼리 파라미터에 대한 설명을 담고 있는 객체입니다.
 * 필드 설명을 포함한 `ParameterDescriptor` 객체를 래핑합니다.
 */
open class Param(
    // 파라미터에 대한 설명을 담고 있는 ParameterDescriptor 객체
    val descriptor: ParameterDescriptor,
) {
    open infix fun optional(value: Boolean): Param {
        if (value) descriptor.optional()
        return this
    }
}

/**
 * 경로 파라미터에 대한 설명을 설정합니다.
 *
 * @param description 경로 파라미터에 대한 설명
 * @return 설정된 경로 파라미터 객체
 */
infix fun String.means(description: String): Param {
    return createField(this, description)
}

/**
 * 파라미터를 생성하고, 설명을 추가하여 `Param` 객체를 반환합니다.
 *
 * @param value 파라미터 이름
 * @param description 파라미터에 대한 설명
 * @param optional 해당 파라미터가 선택 사항인지를 나타내는 플래그 (기본값은 false)
 * @return 생성된 `Param` 객체
 */
private fun createField(
    value: String,
    description: String,
): Param {
    val descriptor =
        RequestDocumentation
            .parameterWithName(value) // 파라미터 이름을 설정
            .description(description) // 파라미터 설명을 설정

    return Param(descriptor) // 설정된 파라미터 설명을 가진 Param 객체 반환
}
