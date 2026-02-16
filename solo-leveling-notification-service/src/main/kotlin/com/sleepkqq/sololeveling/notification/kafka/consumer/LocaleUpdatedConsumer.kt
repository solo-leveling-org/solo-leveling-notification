package com.sleepkqq.sololeveling.notification.kafka.consumer

import com.sleepkqq.sololeveling.avro.config.consumer.AbstractKafkaConsumer
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.idempotency.IdempotencyService
import com.sleepkqq.sololeveling.avro.notification.NotificationSource
import com.sleepkqq.sololeveling.avro.user.LocaleUpdatedEvent
import com.sleepkqq.sololeveling.notification.kafka.producer.NotificationProducer
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