package com.yapp.brake.support

import com.yapp.brake.auth.controller.AuthController
import com.yapp.brake.auth.service.AuthUseCase
import com.yapp.brake.common.filter.JwtAuthenticationFilter
import com.yapp.brake.group.controller.GroupController
import com.yapp.brake.group.service.GroupUseCase
import com.yapp.brake.member.controller.MemberController
import com.yapp.brake.member.service.MemberUseCase
import com.yapp.brake.session.controller.SessionController
import com.yapp.brake.session.service.SessionUseCase
import com.yapp.brake.statistic.controller.StatisticController
import com.yapp.brake.statistic.service.StatisticUseCase
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
        MemberController::class,
        GroupController::class,
        SessionController::class,
        StatisticController::class,
    ],
)
abstract class RestApiTestBase {
    @MockitoBean
    lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @MockitoBean
    lateinit var authUseCase: AuthUseCase

    @MockitoBean
    lateinit var memberUseCase: MemberUseCase

    @MockitoBean
    lateinit var groupUseCase: GroupUseCase

    @MockitoBean
    lateinit var sessionUseCase: SessionUseCase

    @MockitoBean
    lateinit var statisticUseCase: StatisticUseCase

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
