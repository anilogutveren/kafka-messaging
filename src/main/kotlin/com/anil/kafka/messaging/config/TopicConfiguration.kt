package com.anil.kafka.messaging.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class TopicConfiguration {

    fun newTopic(): NewTopic {
        return TopicBuilder
            .name("my-topic")
            .partitions(3)
            .replicas(3)
            .build()
    }
}