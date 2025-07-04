package com.sleepkqq.sololeveling.notification.routing

import com.sleepkqq.sololeveling.avro.constants.KafkaTaskTopics
import com.sleepkqq.sololeveling.avro.notification.NotificationPriority
import org.springframework.stereotype.Component

@Component
class PriorityBasedRoutingStrategy : NotificationRoutingStrategy {

	companion object {
		private val LOW_PRIORITY_TASK_TOPICS = setOf(KafkaTaskTopics.UI_NOTIFICATION_TOPIC)
		private val MEDIUM_PRIORITY_TASK_TOPICS = setOf(
			KafkaTaskTopics.UI_NOTIFICATION_TOPIC,
			KafkaTaskTopics.TG_NOTIFICATION_TOPIC
		)
	}

	override fun getTopics(priority: NotificationPriority): Set<String> {
		return when (priority) {
			NotificationPriority.LOW -> LOW_PRIORITY_TASK_TOPICS
			NotificationPriority.MEDIUM, NotificationPriority.HIGH -> MEDIUM_PRIORITY_TASK_TOPICS
		}
	}
}
