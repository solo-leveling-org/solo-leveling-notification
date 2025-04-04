package com.sleepkqq.sololeveling.notification.routing;

import static com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics.TG_NOTIFICATION_TOPIC;
import static com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics.UI_NOTIFICATION_TOPIC;

import com.sleepkqq.sololeveling.avro.notification.NotificationPriority;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriorityBasedRoutingStrategy implements NotificationRoutingStrategy {

  @Override
  public List<String> getTopics(NotificationPriority priority) {
    return switch (priority) {
      case LOW -> List.of(UI_NOTIFICATION_TOPIC);
      case MEDIUM, HIGH -> List.of(UI_NOTIFICATION_TOPIC, TG_NOTIFICATION_TOPIC);
    };
  }
}
