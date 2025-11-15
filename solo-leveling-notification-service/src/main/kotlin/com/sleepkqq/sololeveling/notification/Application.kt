package com.sleepkqq.sololeveling.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling

@EnableRetry
@EnableScheduling
@SpringBootApplication
class Application : SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
