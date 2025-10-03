package com.sleepkqq.sololeveling.notification.kafka

import com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds
import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.notification.SendNotificationEvent
import com.sleepkqq.sololeveling.notification.mapper.AvroMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Suppress("unused")
@Service
class SendNotificationConsumer(
	private val receiveNotificationProducer: ReceiveNotificationProducer,
	private val avroMapper: AvroMapper
) {

	private val log = LoggerFactory.getLogger(SendNotificationConsumer::class.java)

	@KafkaListener(
		topics = [KafkaTaskTopics.SEND_NOTIFICATION_TOPIC],
		groupId = KafkaGroupIds.NOTIFICATION_GROUP_ID
	)
	fun listen(event: SendNotificationEvent) {
		log.info("<< Start sending notification | transactionId={}", event.transactionId)
		val receiveNotificationEvent = avroMapper.map(event)
		receiveNotificationProducer.send(event.priority, receiveNotificationEvent)
	}
}
