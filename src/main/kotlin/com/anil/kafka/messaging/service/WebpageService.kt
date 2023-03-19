package com.anil.kafka.messaging.service

import jakarta.servlet.http.HttpServletRequest

interface WebpageService {
    fun getRequestDetails(request: HttpServletRequest)
}