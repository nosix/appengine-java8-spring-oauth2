package org.musyozoku.appengine.mapper.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDate

class LocalDateDeserializer : JsonDeserializer<LocalDate>() {

    private val deserializer = ZonedDateTimeDeserializer()

    override fun deserialize(p: JsonParser, ctx: DeserializationContext): LocalDate {
        val dateTime = deserializer.deserialize(p, ctx)
        return dateTime.toLocalDate()
    }
}