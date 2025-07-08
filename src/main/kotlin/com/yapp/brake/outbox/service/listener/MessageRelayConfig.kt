package com.yapp.brake.outbox.service.listener

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy

@EnableAsync
@Configuration
@EnableScheduling
class MessageRelayConfig {
    @Bean
    fun messageRelayPublishEventExecutor(): Executor =
        ThreadPoolTaskExecutor()
            .apply {
                corePoolSize = POOL_SIZE
                maxPoolSize = MAX_POOL_SIZE
                queueCapacity = QUEUE_SIZE
                setThreadNamePrefix("pub-event-")
                setWaitForTasksToCompleteOnShutdown(true)
                setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS)
                setRejectedExecutionHandler(CallerRunsPolicy())
            }

    @Bean
    fun messageRelayPublishPendingEventExecutor(): Executor = Executors.newSingleThreadScheduledExecutor()

    companion object {
        private const val POOL_SIZE = 20
        private const val MAX_POOL_SIZE = 50
        private const val QUEUE_SIZE = 100
        private const val AWAIT_TERMINATION_SECONDS = 10
    }
}
