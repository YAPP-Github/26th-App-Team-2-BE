package com.yapp.brake.support.restdocs

import org.springframework.restdocs.payload.JsonFieldType

/**
 * API 문서에서 사용될 필드 타입을 나타내는 sealed 클래스입니다.
 * 각 필드는 `JsonFieldType`에 해당하는 타입을 가지고 있으며,
 * 이는 필드가 나타내는 데이터의 형태를 정의합니다.
 */
sealed class FieldType(
    val type: JsonFieldType,
)

data object ARRAY : FieldType(JsonFieldType.ARRAY)

data object BOOLEAN : FieldType(JsonFieldType.BOOLEAN)

data object OBJECT : FieldType(JsonFieldType.OBJECT)

data object NUMBER : FieldType(JsonFieldType.NUMBER)

data object NULL : FieldType(JsonFieldType.NULL)

data object STRING : FieldType(JsonFieldType.STRING)

data object ANY : FieldType(JsonFieldType.VARIES)

data object DATE : FieldType(JsonFieldType.STRING)

data object DATETIME : FieldType(JsonFieldType.STRING)

data object TIME : FieldType(JsonFieldType.STRING)
