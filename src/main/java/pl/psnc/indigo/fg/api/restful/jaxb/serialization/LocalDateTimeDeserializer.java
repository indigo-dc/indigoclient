package pl.psnc.indigo.fg.api.restful.jaxb.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Deserializes a string containing date in ISO 8601 format.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public final LocalDateTime deserialize(final JsonParser jsonParser,
                                           final DeserializationContext
                                                   deserializationContext)
            throws IOException, JsonProcessingException {
        String dateString = jsonParser.readValueAs(String.class);
        return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
    }
}
