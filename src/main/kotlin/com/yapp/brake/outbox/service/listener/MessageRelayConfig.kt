package com.yapp.brake.outbox.service.listener

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@EnableAsync
@Configuration
@EnableScheduling
class MessageRelayConfig {
    @Bean
    fun messageRelayPublishEventExecutor(): Executor =
        ThreadPoolTaskExecutor()
            .apply {
                corePoolSize = 20
                maxPoolSize = 50
                queueCapacity = 100
                setThreadNamePrefix("pub-event-")
            }

    @Bean
    fun messageRelayPublishPendingEventExecutor(): Executor = Executors.newSingleThreadScheduledExecutor()
}
