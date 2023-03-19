package com.anil.kafka.messaging.controller

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.TestPropertySource
import org.springframework.web.util.UriComponentsBuilder


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = ["my-topic"], partitions = 3, brokerProperties = ["listeners=PLAINTEXT://localhost:9092"])
@TestPropertySource(
    properties = ["spring.kafka.producer.bootstrap-servers=\${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=\${spring.embedded.kafka.brokers}"]
)
class ResourceControllerTest {

    @Autowired
    lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    private lateinit var consumer: Consumer<String, String>

    @Autowired
    lateinit var restTemplate: TestRestTemplate

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
    fun `Given a request with cookie header, When controller is called, Consumer receives a single record`() {

        val uri = UriComponentsBuilder.fromUriString("/callPage")
            .toUriString()

        val headers = HttpHeaders().add("Cookie", "cookie_name=cookie_value")

        val requestEntity = HttpEntity<Any>(headers)

        val response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String::class.java)

        assertEquals(HttpStatus.OK, response?.statusCode)

        val consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "my-topic")

        val recordValue = consumerRecord.value()

        assertTrue(recordValue.isNotEmpty())
    }
}