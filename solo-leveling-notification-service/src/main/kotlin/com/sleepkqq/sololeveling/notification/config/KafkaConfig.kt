package com.sleepkqq.sololeveling.notification.config

import com.sleepkqq.sololeveling.avro.config.DefaultKafkaConfig
import com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent
import com.sleepkqq.sololeveling.avro.notification.SendNotificationEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@EnableKafka
@Configuration
@Suppress("unused")
class KafkaConfig(
	@Value("\${spring.kafka.bootstrap-servers}") bootstrapServers: String,
	@Value("\${spring.kafka.properties.schema.registry.url}") schemaRegistryUrl: String
) : DefaultKafkaConfig(bootstrapServers, schemaRegistryUrl) {

	@Bean
	fun producerFactoryReceiveNotificationEvent(): ProducerFactory<String, ReceiveNotificationEvent> =
		createProducerFactory()

	@Bean
	fun kafkaTemplateReceiveNotificationEvent(
		producerFactory: ProducerFactory<String, ReceiveNotificationEvent>
	): KafkaTemplate<String, ReceiveNotificationEvent> = createKafkaTemplate(producerFactory)

	@Bean
	fun consumerFactoryGenerateTasksEvent(): ConsumerFactory<String, SendNotificationEvent> =
		createConsumerFactory(KafkaGroupIds.NOTIFICATION_GROUP_ID)

	@Bean
	fun kafkaListenerContainerFactory(
		consumerFactory: ConsumerFactory<String, SendNotificationEvent>
	): ConcurrentKafkaListenerContainerFactory<String, SendNotificationEvent> =
		createKafkaListenerContainerFactory(consumerFactory)
}
