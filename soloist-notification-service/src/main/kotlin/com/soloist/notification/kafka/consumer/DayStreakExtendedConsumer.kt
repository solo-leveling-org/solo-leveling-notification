package com.soloist.notification.kafka.consumer

import com.soloist.avro.config.consumer.AbstractKafkaConsumer
import com.soloist.avro.constants.KafkaTaskTopics
import com.soloist.avro.idempotency.IdempotencyService
import com.soloist.avro.notification.NotificationSource
import com.soloist.avro.player.DayStreakExtendedEvent
import com.soloist.notification.kafka.producer.NotificationProducer
import com.soloist.notification.service.i18n.LocalizationCode
import com.soloist.notification.service.i18n.I18nService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service
import java.util.*

@Service
class DayStreakExtendedConsumer(
	private val notificationProducer: NotificationProducer,
	private val i18nService: I18nService,
	idempotencyService: IdempotencyService
) : AbstractKafkaConsumer<DayStreakExtendedEvent>(
	idempotencyService = idempotencyService,
	log = LoggerFactory.getLogger(DayStreakExtendedConsumer::class.java)
) {

	@RetryableTopic
	@KafkaListener(
		topics = [KafkaTaskTopics.DAY_STREAK_EXTENDED_TOPIC],
		groupId = $$"${spring.kafka.avro.group-id}"
	)
	fun listen(event: DayStreakExtendedEvent) {
		consumeWithIdempotency(event)
	}

	override fun getTxId(event: DayStreakExtendedEvent): String = event.txId

	override fun processEvent(event: DayStreakExtendedEvent) {
		notificationProducer.send(
			txId = UUID.fromString(event.txId),
			userId = event.userId,
			message = i18nService.getMessage(LocalizationCode.DAY_STREAK_EXTENDED_INFO),
			source = NotificationSource.DAY_STREAK,
			topics = setOf(KafkaTaskTopics.UI_NOTIFICATION_TOPIC),
		)
	}
}