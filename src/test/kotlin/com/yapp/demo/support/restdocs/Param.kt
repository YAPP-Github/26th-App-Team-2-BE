package com.yapp.demo.support.restdocs

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.request.QueryParametersSnippet
import org.springframework.restdocs.request.RequestDocumentation

/**
 * 경로 또는 쿼리 파라미터에 대한 설명을 담고 있는 객체입니다.
 * 필드 설명을 포함한 `ParameterDescriptor` 객체를 래핑합니다.
 */
open class Param(
    // 파라미터에 대한 설명을 담고 있는 ParameterDescriptor 객체
    val descriptor: ParameterDescriptor,
)

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
    optional: Boolean = false,
): Param {
    val descriptor =
        RequestDocumentation
            .parameterWithName(value) // 파라미터 이름을 설정
            .description(description) // 파라미터 설명을 설정

    // 파라미터가 선택 사항인 경우 optional로 설정
    if (optional) descriptor.optional()

    return Param(descriptor) // 설정된 파라미터 설명을 가진 Param 객체 반환
}

/**
 * 경로 파라미터들을 `PathParametersSnippet` 객체로 변환하여 반환합니다.
 *
 * @param params 경로 파라미터들의 배열
 * @return `PathParametersSnippet` 객체
 */
fun pathParameters(vararg params: Param): PathParametersSnippet {
    return RequestDocumentation.pathParameters(params.map { it.descriptor })
}

/**
 * 쿼리 파라미터들을 `QueryParametersSnippet` 객체로 변환하여 반환합니다.
 *
 * @param params 쿼리 파라미터들의 배열
 * @return `QueryParametersSnippet` 객체
 */
fun queryParameters(vararg params: Param): QueryParametersSnippet {
    return RequestDocumentation.queryParameters(params.map { it.descriptor })
}
