package com.yapp.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.security.PrivateKey

@SpringBootTest
class DemoApplicationTests {
    @MockitoBean
    private lateinit var applePrivateKey: PrivateKey

    @Test
    fun contextLoads() {
    }
}
