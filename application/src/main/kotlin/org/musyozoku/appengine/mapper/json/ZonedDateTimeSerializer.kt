package org.musyozoku.appengine.mapper.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeSerializer : JsonSerializer<ZonedDateTime>() {

    override fun serialize(value: ZonedDateTime, gen: JsonGenerator, serializers: SerializerProvider) {
        val utc = ZonedDateTime.ofInstant(value.toInstant(), ZoneId.of("UTC"))
        gen.writeString(utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")))
    }
}