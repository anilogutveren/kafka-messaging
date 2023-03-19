package com.anil.kafka.messaging.service

import com.anil.kafka.messaging.domain.RequestEventDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class EventProducerService {

    private val log = LoggerFactory.getLogger(KafkaListenerService::class.java)



    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun sendNewEvent(topic: NewTopic, kMessage: String) {
        // Approach 1
        kafkaTemplate.send(topic.name(), UUID.randomUUID().toString(), kMessage)
    }

    fun sendNewEventAsRecord(topic: NewTopic, kMessage: String) {
        val record = ProducerRecord<String, String>(topic.name(), kMessage)

        try {
            kafkaTemplate.send(record)
        } catch (ex: Throwable) {
            handleFailure(ex)
        }

        log.info("Message Sent SuccessFully")
    }

    fun sendNewRequestEventAsRecord(newRequestEventDto: RequestEventDto) {
        val key = newRequestEventDto.id
        val value = objectMapper.writeValueAsString(newRequestEventDto);

        val topic = "my-topic"

        val record = ProducerRecord(topic, key, value)

        try {
            kafkaTemplate.send(record)
        } catch (ex: Throwable) {
            handleFailure(ex)
        }

        log.info("Message Sent SuccessFully")
    }

    private fun handleFailure(ex: Throwable) {
        log.error("Error Sending the Message and the exception is {}", ex.message)
        try {
            throw ex
        } catch (throwable: Throwable) {
            log.error("Error in OnFailure: {}", throwable.message)
        }
    }
}