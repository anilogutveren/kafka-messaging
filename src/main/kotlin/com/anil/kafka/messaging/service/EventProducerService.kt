package com.anil.kafka.messaging.service

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class EventProducerService {

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, String>

    fun sendNewEvent(topic: NewTopic, kMessage: String) {

        kafkaTemplate.send("new-example-topic", UUID.randomUUID().toString(), kMessage)
    }
}