package com.sleepkqq.sololeveling.notification.kafka

import com.sleepkqq.sololeveling.avro.notification.NotificationPriority
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent
import com.sleepkqq.sololeveling.notification.routing.NotificationRoutingStrategy
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ReceiveNotificationProducer(
	private val kafkaTemplate: KafkaTemplate<String, Any>,
	private val routingStrategy: NotificationRoutingStrategy
) {

	fun send(priority: NotificationPriority, event: ReceiveNotificationEvent) =
		routingStrategy.getTopics(priority)
			.forEach { kafkaTemplate.send(it, event) }
}
