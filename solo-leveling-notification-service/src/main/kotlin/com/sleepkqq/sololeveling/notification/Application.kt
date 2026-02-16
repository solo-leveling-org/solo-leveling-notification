package com.sleepkqq.sololeveling.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class Application : SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
