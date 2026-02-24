package com.soloist.notification.kafka.consumer

import com.soloist.avro.config.consumer.AbstractKafkaConsumer
import com.soloist.avro.constants.KafkaTaskTopics
import com.soloist.avro.idempotency.IdempotencyService
import com.soloist.avro.notification.NotificationSource
import com.soloist.avro.user.LocaleUpdatedEvent
import com.soloist.notification.kafka.producer.NotificationProducer
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocaleUpdatedConsumer(
	private val notificationProducer: NotificationProducer,
	idempotencyService: IdempotencyService
) : AbstractKafkaConsumer<LocaleUpdatedEvent>(
	idempotencyService = idempotencyService,
	log = LoggerFactory.getLogger(LocaleUpdatedConsumer::class.java)
) {

	@RetryableTopic
	@KafkaListener(
		topics = [KafkaTaskTopics.LOCALE_UPDATED_TOPIC],
		groupId = $$"${spring.kafka.avro.group-id}"
	)
	fun listen(event: LocaleUpdatedEvent) {
		consumeWithIdempotency(event)
	}

	override fun getTxId(event: LocaleUpdatedEvent): String = event.txId

	override fun processEvent(event: LocaleUpdatedEvent) {
		notificationProducer.send(
			txId = UUID.fromString(event.txId),
			userId = event.userId,
			source = NotificationSource.LOCALE,
			topics = setOf(KafkaTaskTopics.UI_NOTIFICATION_TOPIC, KafkaTaskTopics.TG_NOTIFICATION_TOPIC),
		)
	}
}