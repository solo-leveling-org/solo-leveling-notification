package com.sleepkqq.sololeveling.notification.kafka;

import static com.sleepkqq.sololeveling.avro.constants.KafkaGroupIds.NOTIFICATION_GROUP_ID;
import static com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics.SEND_NOTIFICATION_TOPIC;

import com.sleepkqq.sololeveling.avro.notification.ReceiveNotificationEvent;
import com.sleepkqq.sololeveling.avro.notification.SendNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendNotificationConsumer {

  private final ReceiveNotificationProducer receiveNotificationProducer;

  @KafkaListener(topics = SEND_NOTIFICATION_TOPIC, groupId = NOTIFICATION_GROUP_ID)
  public void listen(SendNotificationEvent event) {
    log.info("<< Start sending notification | transactionId={}", event.getTransactionId());
    var receiveNotificationEvent = new ReceiveNotificationEvent(
        event.getTransactionId(),
        event.getUserId(),
        event.getNotification()
    );
    receiveNotificationProducer.send(event.getPriority(), receiveNotificationEvent);
  }
}
