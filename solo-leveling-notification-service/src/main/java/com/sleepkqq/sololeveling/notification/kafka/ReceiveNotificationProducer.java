package com.sleepkqq.sololeveling.notification.kafka;

import com.sleepkqq.sololeveling.avro.notification.NotificationPriority;
import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent;
import com.sleepkqq.sololeveling.notification.routing.NotificationRoutingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveNotificationProducer {

  private final KafkaTemplate<String, ReceiveNotificationEvent> kafkaTemplate;
  private final NotificationRoutingStrategy routingStrategy;

  public void send(NotificationPriority priority, ReceiveNotificationEvent event) {
    routingStrategy.getTopics(priority)
        .forEach(topic -> sendToTopic(topic, event));
  }

  private void sendToTopic(String topic, ReceiveNotificationEvent event) {
    kafkaTemplate.send(topic, event);
    log.info(">> {} sent | transactionId={}", topic, event.getTransactionId());
  }
}
