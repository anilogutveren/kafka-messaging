package com.anil.kafka.messaging.client

import com.azure.messaging.eventhubs.EventData
import com.azure.messaging.eventhubs.EventHubClientBuilder
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.concurrent.Executors

class EventHubClient {

    private val connectionString = ""
    private val eventHubName = "newseventshub"
    private val executorService = Executors.newSingleThreadExecutor()
    private val producer = EventHubClientBuilder()
        .connectionString(connectionString, eventHubName)
        .buildProducerClient()

    fun sendEvent(event: String) {
        val eventData = serializeEvent(event)
        val eventBatch = producer.createBatch()
        eventBatch.tryAdd(EventData(eventData))
        producer.send(eventBatch)
    }

    private fun serializeEvent(event: String): ByteArray {
        val objectMapper = ObjectMapper()
        try {
            return objectMapper.writeValueAsBytes(event)
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Unable to serialize event", e)
        }
    }

    fun close() {
        producer.close()
        executorService.shutdown()
    }
}
