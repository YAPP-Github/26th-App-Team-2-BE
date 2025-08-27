package com.yapp.brake

import com.yapp.brake.oauth.apple.ApplePrivateKeyLoader
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest
class BrakeApplicationTests {
    @MockitoBean
    lateinit var applePrivateKeyLoader: ApplePrivateKeyLoader

    @Test
    fun contextLoads() {
    }
}
