import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.yapp.brake.support.restdocs.Field
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet

/**
 * 요청 본문에 포함된 필드들을 설정한 RequestFieldsSnippet을 반환합니다.
 *
 * @param fields 설정할 필드 객체들
 * @return 필드들이 설정된 RequestFieldsSnippet 객체
 */
fun requestBody(vararg fields: Field): RequestFieldsSnippet {
    return PayloadDocumentation.requestFields(fields.map { it.descriptor })
}

fun ResourceSnippetParametersBuilder.requestBody(vararg fields: Field): ResourceSnippetParametersBuilder {
    return requestFields(fields.map { it.descriptor })
}

/**
 * 응답 본문에 포함된 필드들을 설정한 ResponseFieldsSnippet을 반환합니다.
 *
 * @param fields 설정할 필드 객체들
 * @return 필드들이 설정된 ResponseFieldsSnippet 객체
 */
fun responseBody(vararg fields: Field): ResponseFieldsSnippet {
    return PayloadDocumentation.responseFields(fields.map { it.descriptor })
}

fun ResourceSnippetParametersBuilder.responseBody(vararg fields: Field): ResourceSnippetParametersBuilder {
    return responseFields(fields.map { it.descriptor })
}
