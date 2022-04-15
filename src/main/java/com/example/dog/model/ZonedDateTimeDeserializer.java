package com.example.dog.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The standard Jackson's implementation is not appropriate since it looses the original timezone information
 * (DateTimeFormatter uses ISO_OFFSET_DATE_TIME constant, so dateOfBirth.zone.id is always "UTC")
 */
public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return ZonedDateTime.parse(jp.getText(), DateTimeFormatter.ISO_DATE_TIME);
    }
}
