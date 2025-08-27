package com.yapp.brake.support.restdocs

/**
 * RestDocs에서 사용되는 속성 키를 정의하는 객체입니다.
 * 각 상수는 필드 설명에 포함될 특정 속성의 키를 나타냅니다.
 */
internal object RestDocsAttributeKeys {
    /**
     * 필드의 'format' 속성 키.
     * 예: "date-time", "string" 등과 같은 형식을 나타낼 때 사용됩니다.
     */
    const val KEY_FORMAT = "format"

    /**
     * 필드의 'example' 속성 키.
     * 필드의 예시 값을 나타낼 때 사용됩니다. 문서화에서 예시 값을 제공할 때 사용됩니다.
     */
    const val KEY_EXAMPLE = "example"

    /**
     * 필드의 'default' 속성 키.
     * 필드의 기본값을 나타낼 때 사용됩니다. 문서화에서 기본값을 제공할 때 사용됩니다.
     */
    const val KEY_DEFAULT = "default"
}
