package com.sleepkqq.sololeveling.notification.routing

import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.notification.NotificationPriority
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
class PriorityBasedRoutingStrategy : NotificationRoutingStrategy {

	override fun getTopics(priority: NotificationPriority): Set<String> =
		when (priority) {
			NotificationPriority.LOW -> setOf(KafkaTaskTopics.UI_NOTIFICATION_TOPIC)
			NotificationPriority.MEDIUM, NotificationPriority.HIGH -> setOf(
				KafkaTaskTopics.UI_NOTIFICATION_TOPIC,
				KafkaTaskTopics.TG_NOTIFICATION_TOPIC
			)
		}
}
