package com.anil.kafka.messaging.domain

import org.jetbrains.annotations.NotNull
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

data class RequestEventDto(


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: String,

    @NotNull
    var cookie: String

)