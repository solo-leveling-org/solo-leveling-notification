package com.sleepkqq.sololeveling.notification.routing;

import com.sleepkqq.sololeveling.avro.notification.NotificationPriority;
import java.util.List;

public interface NotificationRoutingStrategy {

  List<String> getTopics(NotificationPriority priority);
}
