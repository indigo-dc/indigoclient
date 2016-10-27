package pl.psnc.indigo.fg.api.restful.jaxb.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Deserializes a string containing date in ISO 8601 format.
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(final LocalDateTime localDateTime,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(
                localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
