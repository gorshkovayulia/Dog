package com.example.dog;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The standard Jackson's implementation is not appropriate since it looses the original timezone information
 * (DateTimeFormatter uses ISO_OFFSET_DATE_TIME constant, so dateOfBirth.zone.id is always "UTC")
 */
public class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
