package com.sleepkqq.sololeveling.notification.config;

import static com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds.NOTIFICATION_GROUP_ID;

import com.sleepkqq.sololeveling.avro.config.DefaultKafkaConfig;
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent;
import com.sleepkqq.sololeveling.avro.notification.SendNotificationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
@SuppressWarnings("unused")
public class KafkaConfig extends DefaultKafkaConfig {

  public KafkaConfig(
      @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
      @Value("${spring.kafka.properties.schema.registry.url}") String schemaRegistryUrl
  ) {
    super(bootstrapServers, schemaRegistryUrl);
  }

  @Bean
  public ProducerFactory<String, ReceiveNotificationEvent> producerFactoryReceiveNotificationEvent() {
    return createProducerFactory();
  }

  @Bean
  public KafkaTemplate<String, ReceiveNotificationEvent> kafkaTemplateReceiveNotificationEvent(
      ProducerFactory<String, ReceiveNotificationEvent> producerFactory
  ) {
    return createKafkaTemplate(producerFactory);
  }

  @Bean
  public ConsumerFactory<String, SendNotificationEvent> consumerFactoryGenerateTasksEvent() {
    return createConsumerFactory(NOTIFICATION_GROUP_ID);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, SendNotificationEvent> kafkaListenerContainerFactory(
      ConsumerFactory<String, SendNotificationEvent> consumerFactory
  ) {
    return createKafkaListenerContainerFactory(consumerFactory);
  }
}
