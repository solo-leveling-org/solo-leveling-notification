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
		kafkaTemplate.send(topic, event).whenComplete { _, ex ->
			if (ex == null) {
				log.info("<< Notification sent to {} | txId={}", topic, event.txId)
			} else {
				log.error("Failed to send notification to {} | txId={}", topic, event.txId, ex)
			}
		}
	}
}
