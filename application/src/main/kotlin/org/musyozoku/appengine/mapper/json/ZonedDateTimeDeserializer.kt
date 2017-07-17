package org.musyozoku.appengine.mapper.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeDeserializer : JsonDeserializer<ZonedDateTime>() {

    override fun deserialize(p: JsonParser, ctx: DeserializationContext): ZonedDateTime {
        val utc = ZonedDateTime.parse(p.text, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"))
        return ZonedDateTime.ofInstant(utc.toInstant(), ZoneId.systemDefault())
    }
}