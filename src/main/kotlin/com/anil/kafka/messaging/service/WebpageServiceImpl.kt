package com.anil.kafka.messaging.service

import com.anil.kafka.messaging.domain.RequestEventDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class WebpageServiceImpl : WebpageService {

    @Autowired
    private lateinit var eventProducerService: EventProducerService

    override fun getRequestDetails(request: HttpServletRequest) {
        val newRequestEventDto = RequestEventDto(id = UUID.randomUUID().toString(), cookie = "testCookie")

        eventProducerService.sendNewRequestEventAsRecord(newRequestEventDto)
    }
}
