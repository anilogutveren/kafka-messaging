package com.anil.kafka.messaging.controller


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/kafkamessage")
class ResourceController {

    @Value("messaging.kafka.topic")
    private lateinit var topic: String

    @Autowired
    private lateinit var  kafkaTemplate: KafkaTemplate<String, String>

    @PostMapping
    fun sendMessage(@RequestBody kMessage: String) {
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), kMessage)
    }
}