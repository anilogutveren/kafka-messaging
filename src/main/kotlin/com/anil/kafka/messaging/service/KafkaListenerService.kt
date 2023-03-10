package com.anil.kafka.messaging.service

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service


@Service
class KafkaListenerService {

    private val log = LoggerFactory.getLogger(KafkaListenerService::class.java)

    @KafkaListener(
        topics = ["messaging.kafka.topic"],
        groupId = "messaging.kafka.group.id"
    )
    fun listen(message: String) {
        log.info(
            "Message received.. MessageID : {} Message: {} Date : {}",
            message
        )
    }
}
