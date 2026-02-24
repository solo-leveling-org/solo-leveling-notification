package com.soloist.notification.kafka.producer

import com.soloist.avro.notification.Notification
import com.soloist.avro.notification.NotificationEvent
import com.soloist.avro.notification.NotificationSource
import com.soloist.avro.notification.NotificationType
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationProducer(
	private val kafkaTemplate: KafkaTemplate<String, Any>
) {

	fun send(
		txId: UUID = UUID.randomUUID(),
		userId: Long,
		message: String? = null,
		type: NotificationType = NotificationType.INFO,
		source: NotificationSource,
		topics: Set<String>
	) {
		val notification = Notification(message, type, source)
		val event = NotificationEvent(txId.toString(), userId, notification)

		topics.forEach { kafkaTemplate.send(it, event.txId, event) }
	}
}
