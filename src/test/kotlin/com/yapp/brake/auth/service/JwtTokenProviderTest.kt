package com.yapp.brake.auth.service

import com.yapp.brake.common.config.properties.JwtProperties
import com.yapp.brake.common.constants.TOKEN_TYPE_ACCESS
import com.yapp.brake.common.constants.TOKEN_TYPE_REFRESH
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.deviceprofile.infrastructure.DeviceProfileReader
import com.yapp.brake.member.infrastructure.jpa.MemberJpaReader
import com.yapp.brake.support.fixture.model.deviceProfileFixture
import com.yapp.brake.support.fixture.model.memberFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.core.authority.SimpleGrantedAuthority
import kotlin.test.Test
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class JwtTokenProviderTest {
    private val secretKey = "mysecretkey123456789012345678901234mysecretkey123456789012345678901234"
    private val jwtProperties =
        JwtProperties(
            secret = secretKey,
            accessTokenExpiryTime = 3600,
            refreshTokenExpiryTime = 604800,
        )

    private lateinit var jwtTokenProvider: JwtTokenProvider

    private val memberReader = mock<MemberJpaReader>()
    private val deviceProfileReader = mock<DeviceProfileReader>()

    @BeforeEach
    fun setUp() {
        jwtTokenProvider = JwtTokenProvider(jwtProperties, memberReader, deviceProfileReader)
    }

    @Test
    fun `AccessToken과 RefreshToken 생성 후 memberId 추출이 동일해야 한다`() {
        val memberId = 12345L
        val deviceProfileId = 1L

        val accessToken = jwtTokenProvider.generateAccessToken(memberId, deviceProfileId)
        val refreshToken = jwtTokenProvider.generateRefreshToken(memberId, deviceProfileId)

        val extractedMemberIdFromAccess = jwtTokenProvider.extractMemberId(accessToken, TOKEN_TYPE_ACCESS)
        val extractedMemberIdFromRefresh = jwtTokenProvider.extractMemberId(refreshToken, TOKEN_TYPE_REFRESH)

        assertEquals(memberId, extractedMemberIdFromAccess)
        assertEquals(memberId, extractedMemberIdFromRefresh)
    }

    @Test
    fun `AccessToken과 RefreshToken 생성 후 deviceProfileId 추출이 동일해야 한다`() {
        val memberId = 12345L
        val deviceProfileId = 1L

        val accessToken = jwtTokenProvider.generateAccessToken(memberId, deviceProfileId)
        val refreshToken = jwtTokenProvider.generateRefreshToken(memberId, deviceProfileId)

        val extractedMemberIdFromAccess = jwtTokenProvider.extractProfileId(accessToken)
        val extractedMemberIdFromRefresh = jwtTokenProvider.extractProfileId(refreshToken)

        assertEquals(deviceProfileId, extractedMemberIdFromAccess)
        assertEquals(deviceProfileId, extractedMemberIdFromRefresh)
    }

    @Test
    fun `잘못된 토큰을 넣으면 TOKEN_INVALID 예외가 발생해야 한다`() {
        val invalidToken = "this.is.not.a.valid.token"

        val exception =
            assertThrows<CustomException> {
                jwtTokenProvider.extractMemberId(invalidToken, TOKEN_TYPE_ACCESS)
            }

        assertEquals(ErrorCode.TOKEN_INVALID, exception.errorCode)
    }

    @Test
    fun `만료된 토큰을 넣으면 TOKEN_EXPIRED 예외가 발생해야 한다`() {
        val memberId = 12345L
        val deviceProfileId = 1L
        val expiredJwtProperties =
            JwtProperties(
                secret = secretKey,
                accessTokenExpiryTime = -1,
                refreshTokenExpiryTime = -1,
            )

        val expiredTokenProvider = JwtTokenProvider(expiredJwtProperties, memberReader, deviceProfileReader)
        val token = expiredTokenProvider.generateAccessToken(memberId, deviceProfileId)

        val exception =
            assertThrows<CustomException> {
                expiredTokenProvider.extractMemberId(token, TOKEN_TYPE_ACCESS)
            }

        assertEquals(ErrorCode.TOKEN_EXPIRED, exception.errorCode)
    }

    @Test
    fun `getAuthentication은 유효한 memberId와 deviceProfileId에 대해 Authentication 객체를 반환해야 한다`() {
        // given
        val memberId = 123L
        val member = memberFixture(id = memberId)
        val deviceProfileId = 1L
        val deviceProfile = deviceProfileFixture(id = deviceProfileId, memberId = memberId)

        whenever(memberReader.findById(memberId)).thenReturn(member)
        whenever(deviceProfileReader.getById(deviceProfileId)).thenReturn(deviceProfile)

        // when
        val authentication = jwtTokenProvider.getAuthentication(memberId, deviceProfileId)

        // then
        assertEquals(memberId, authentication.principal)
        assertEquals(deviceProfileId, authentication.credentials)
        assertTrue(authentication.authorities.contains(SimpleGrantedAuthority(member.role.type)))
        assertTrue(authentication.authorities.contains(SimpleGrantedAuthority(member.state.name)))
    }

    @Test
    fun `JwtTokenProvider는 토큰으로 부터 만료시간을 추출할 수 있다`() {
        val memberId = 12345L
        val deviceProfileId = 1L

        val accessToken = jwtTokenProvider.generateAccessToken(memberId, deviceProfileId)
        val expiration = jwtTokenProvider.extractExpiration(accessToken)

        val now = System.currentTimeMillis()
        val expiryWithinRange = expiration in (now + 1000)..(now + jwtProperties.accessTokenExpiryTime * 1000)

        assertEquals(true, expiryWithinRange)
    }
}
