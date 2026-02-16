package com.sleepkqq.sololeveling.notification.kafka.consumer

import com.sleepkqq.sololeveling.avro.config.consumer.AbstractKafkaConsumer
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.idempotency.IdempotencyService
import com.sleepkqq.sololeveling.avro.notification.NotificationSource
import com.sleepkqq.sololeveling.avro.player.DayStreakExtendedEvent
import com.sleepkqq.sololeveling.notification.kafka.producer.NotificationProducer
import com.sleepkqq.sololeveling.notification.service.i18n.LocalizationCode
import com.sleepkqq.sololeveling.notification.service.i18n.I18nService
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