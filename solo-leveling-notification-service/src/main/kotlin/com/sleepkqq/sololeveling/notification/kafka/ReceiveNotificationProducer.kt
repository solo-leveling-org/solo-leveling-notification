package com.sleepkqq.sololeveling.notification.kafka

import com.sleepkqq.sololeveling.avro.notification.NotificationPriority
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent
import com.sleepkqq.sololeveling.notification.routing.NotificationRoutingStrategy
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ReceiveNotificationProducer(
	private val kafkaTemplate: KafkaTemplate<String, ReceiveNotificationEvent>,
	private val routingStrategy: NotificationRoutingStrategy
) {

	private val log = LoggerFactory.getLogger(ReceiveNotificationProducer::class.java)

	fun send(priority: NotificationPriority, event: ReceiveNotificationEvent) =
		routingStrategy.getTopics(priority)
			.forEach { sendToTopic(it, event) }

	private fun sendToTopic(topic: String, event: ReceiveNotificationEvent) {
		kafkaTemplate.send(topic, event)
		log.info(">> Notification sent to {} | txId={}", topic, event.txId)
	}
}