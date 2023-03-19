package com.anil.kafka.messaging.controller

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.TestPropertySource
import org.springframework.web.util.UriComponentsBuilder


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = ["test-topic"], partitions = 3)
@TestPropertySource(properties = ["spring.kafka.producer.bootstrap-servers=\${spring.embedded.kafka.brokers}",
    "spring.kafka.admin.properties.bootstrap.servers=\${spring.embedded.kafka.brokers}"])
class ResourceControllerTest {

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    private lateinit var consumer: Consumer<String, String>

    @Autowired
    var restTemplate: TestRestTemplate? = null

    @BeforeEach
    fun setUp() {
        val configs: Map<String, Any> = HashMap(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker))

        consumer = DefaultKafkaConsumerFactory(
            configs,
            StringDeserializer(),
            StringDeserializer()
        ).createConsumer()

        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer)
    }

    @AfterEach
    fun tearDown() {
        consumer.close()
    }

    @Test
    @Timeout(5)
    fun testSendReceive() {
        val message = "Hello, world!"

        val request = HttpEntity<Any>(message)

        val uri = UriComponentsBuilder.fromUriString("/kafkamessage")
            .toUriString()

        val response = restTemplate?.exchange(uri, HttpMethod.POST, request, String::class.java)

        assertEquals(HttpStatus.CREATED, response?.statusCode)

        val consumerRecord = KafkaTestUtils.getSingleRecord(consumer,"test-topic")

        val recordValue = consumerRecord.value()

        assertEquals(message, recordValue)

    }
}