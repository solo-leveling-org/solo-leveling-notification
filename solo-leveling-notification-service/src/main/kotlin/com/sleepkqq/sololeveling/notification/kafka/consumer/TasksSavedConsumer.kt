package com.sleepkqq.sololeveling.notification.kafka.consumer

import com.sleepkqq.sololeveling.avro.config.consumer.AbstractKafkaConsumer
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.idempotency.IdempotencyService
import com.sleepkqq.sololeveling.avro.notification.NotificationSource
import com.sleepkqq.sololeveling.avro.task.TasksSavedEvent
import com.sleepkqq.sololeveling.notification.kafka.producer.NotificationProducer
import com.sleepkqq.sololeveling.notification.service.i18n.I18nService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TasksSavedConsumer(
	private val notificationProducer: NotificationProducer,
	private val i18nService: I18nService,
	idempotencyService: IdempotencyService
) : AbstractKafkaConsumer<TasksSavedEvent>(
	idempotencyService = idempotencyService,
	log = LoggerFactory.getLogger(TasksSavedConsumer::class.java)
) {

	@RetryableTopic
	@KafkaListener(
		topics = [KafkaTaskTopics.TASKS_SAVED_TOPIC],
		groupId = $$"${spring.kafka.avro.group-id}"
	)
	fun listen(event: TasksSavedEvent) {
		consumeWithIdempotency(event)
	}

	override fun getTxId(event: TasksSavedEvent): String = event.txId

	override fun processEvent(event: TasksSavedEvent) {
		val message = event.operation
			?.let { "tasks.operation.${it.name.lowercase()}.info" }
			?.let { i18nService.getMessage(it) }

		notificationProducer.send(
			txId = UUID.fromString(event.txId),
			userId = event.userId,
			message = message,
			source = NotificationSource.TASKS,
			topics = setOf(KafkaTaskTopics.UI_NOTIFICATION_TOPIC)
		)
	}
}
