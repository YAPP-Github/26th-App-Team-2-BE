package com.yapp.demo

import com.yapp.demo.auth.internal.ApplePrivateKeyLoader
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest
class DemoApplicationTests {
    @MockitoBean
    private lateinit var applePrivateKeyLoader: ApplePrivateKeyLoader

    @Test
    fun contextLoads() {
    }
}
