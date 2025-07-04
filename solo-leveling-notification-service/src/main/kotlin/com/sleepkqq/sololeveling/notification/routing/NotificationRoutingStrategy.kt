package com.sleepkqq.sololeveling.notification.routing

import com.sleepkqq.sololeveling.avro.notification.NotificationPriority

interface NotificationRoutingStrategy {

	fun getTopics(priority: NotificationPriority): Set<String>
}