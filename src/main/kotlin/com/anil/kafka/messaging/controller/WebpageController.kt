package com.anil.kafka.messaging.controller

import com.anil.kafka.messaging.service.WebpageService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WebpageController(
    private val webpageService: WebpageService,
    private val request: HttpServletRequest
) {
    @GetMapping("/callPage")
    fun showMainPage(): ResponseEntity<Unit> {
        return ResponseEntity.ok(webpageService.getRequestDetails(request))
    }
}
