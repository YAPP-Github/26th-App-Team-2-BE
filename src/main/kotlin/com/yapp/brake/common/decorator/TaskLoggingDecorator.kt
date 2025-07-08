package com.yapp.brake.common.decorator

import org.slf4j.MDC
import org.springframework.core.task.TaskDecorator

class TaskLoggingDecorator : TaskDecorator {
    override fun decorate(runnable: Runnable): Runnable {
        val contextMap: Map<String, String> = MDC.getCopyOfContextMap()

        return Runnable {
            try {
                MDC.setContextMap(contextMap)
                runnable.run()
            } finally {
                MDC.clear()
            }
        }
    }
}
