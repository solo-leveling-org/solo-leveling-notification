package com.sleepkqq.sololeveling.notification

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.ConfluentKafkaContainer

@Suppress("unused")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseTestClass {

	companion object {
		@JvmStatic
		@Container
		val kafkaContainer = ConfluentKafkaContainer("confluentinc/cp-kafka:7.6.0")
			.apply {
				withExposedPorts(9092)
				withNetwork(Network.SHARED)
			}

		@JvmStatic
		@Container
		@ServiceConnection(name = "redis")
		val redisContainer = GenericContainer("redis:7.2.4")
			.apply {
				withExposedPorts(6379)
				withNetwork(Network.SHARED)
			}

		@JvmStatic
		@Container
		val schemaRegistryContainer = GenericContainer("confluentinc/cp-schema-registry:7.6.0")
			.apply {
				withExposedPorts(8081)
				withNetwork(Network.SHARED)
			}
	}
}
