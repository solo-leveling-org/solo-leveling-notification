package com.sleepkqq.sololeveling.notification.routing;

import com.sleepkqq.sololeveling.avro.notification.NotificationPriority;
import java.util.Set;

public interface NotificationRoutingStrategy {

  Set<String> getTopics(NotificationPriority priority);
}
