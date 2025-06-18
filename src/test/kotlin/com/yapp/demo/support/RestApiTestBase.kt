package com.yapp.demo.support

import com.yapp.demo.auth.controller.AuthController
import com.yapp.demo.common.filter.JwtAuthenticationFilter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(
    controllers = [
        AuthController::class,
    ],
)
abstract class RestApiTestBase {
    @MockitoBean
    lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    lateinit var mockMvc: MockMvc

    /**
     * RestDocs 문서화를 위한 `MockMvc` 객체를 설정하는 함수입니다.
     * @param context `WebApplicationContext` 객체
     * @param provider `RestDocumentationContextProvider` 객체
     * @return 설정된 `MockMvc` 객체
     */
    @BeforeEach
    internal fun setUp(
        context: WebApplicationContext,
        provider: RestDocumentationContextProvider,
    ) {
        mockMvc =
            MockMvcBuilders.webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(provider))
                .build()
    }
}
