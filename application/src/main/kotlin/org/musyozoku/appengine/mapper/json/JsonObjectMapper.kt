package org.musyozoku.appengine.mapper.json

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

class JsonObjectMapper : ObjectMapper() {

    init {
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        registerKotlinModule()
        registerModule(JavaTimeModule().apply {
            addSerializer(ZonedDateTime::class.java, ZonedDateTimeSerializer())
            addDeserializer(ZonedDateTime::class.java, ZonedDateTimeDeserializer())
            addSerializer(LocalDate::class.java, LocalDateSerializer())
            addDeserializer(LocalDate::class.java, LocalDateDeserializer())
            addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
            addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())
        })
    }
}