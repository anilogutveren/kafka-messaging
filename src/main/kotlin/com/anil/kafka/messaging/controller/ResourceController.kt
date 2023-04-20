package com.anil.kafka.messaging.controller

import com.anil.kafka.messaging.config.TopicConfiguration
import com.anil.kafka.messaging.service.EventProducerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/kafkamessage")
class ResourceController {

    @Autowired
    private lateinit var topicConfiguration: TopicConfiguration

    @Autowired
    private lateinit var eventProducerService: EventProducerService

    @PostMapping
    fun sendMessage(@RequestBody kMessage: String): ResponseEntity<String> {
        val response = eventProducerService.sendNewEventAsRecord(topicConfiguration.newTopic(), kMessage)
        return ResponseEntity.status(HttpStatus.CREATED).body(response.toString())
    }
}
