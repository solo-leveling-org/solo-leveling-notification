package com.sleepkqq.sololeveling.notification.routing;

import static com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics.TG_NOTIFICATION_TOPIC;
import static com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics.UI_NOTIFICATION_TOPIC;

import com.sleepkqq.sololeveling.avro.notification.NotificationPriority;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriorityBasedRoutingStrategy implements NotificationRoutingStrategy {

  @Override
  public Set<String> getTopics(NotificationPriority priority) {
    return switch (priority) {
      case LOW -> Set.of(UI_NOTIFICATION_TOPIC);
      case MEDIUM, HIGH -> Set.of(UI_NOTIFICATION_TOPIC, TG_NOTIFICATION_TOPIC);
    };
  }
}
