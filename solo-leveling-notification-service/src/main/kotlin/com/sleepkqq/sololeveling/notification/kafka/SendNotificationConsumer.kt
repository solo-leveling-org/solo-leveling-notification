package com.sleepkqq.sololeveling.notification.kafka

import com.sleepkqq.sololeveling.avro.config.consumer.AbstractKafkaConsumer
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.idempotency.IdempotencyService
import com.sleepkqq.sololeveling.avro.notification.SendNotificationEvent
import com.sleepkqq.sololeveling.notification.mapper.AvroMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SendNotificationConsumer(
	private val receiveNotificationProducer: ReceiveNotificationProducer,
	private val avroMapper: AvroMapper,
	idempotencyService: IdempotencyService
) : AbstractKafkaConsumer<SendNotificationEvent>(
	idempotencyService = idempotencyService,
	log = LoggerFactory.getLogger(SendNotificationConsumer::class.java)
) {

	@Transactional
	@RetryableTopic
	@KafkaListener(
		topics = [KafkaTaskTopics.UI_NOTIFICATION_TOPIC],
		groupId = $$"${spring.kafka.avro.group-id}"
	)
	fun listen(event: SendNotificationEvent) {
		consumeWithIdempotency(event)
	}

	override fun getTxId(event: SendNotificationEvent): String = event.txId

	override fun processEvent(event: SendNotificationEvent) {
		log.info("<< Start sending notification | txId={}", event.txId)
		val receiveNotificationEvent = avroMapper.map(event)
		receiveNotificationProducer.send(event.priority, receiveNotificationEvent)
	}
}
