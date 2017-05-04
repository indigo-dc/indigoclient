package pl.psnc.indigo.fg.api.restful.jaxb.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Deserializes a string containing media type information.
 */
public class MediaTypeDeserializer extends JsonDeserializer<MediaType> {
    @Override
    public final MediaType deserialize(final JsonParser jsonParser,
                                       final DeserializationContext
                                               deserializationContext)
            throws IOException {
        JsonNode jsonNode = jsonParser.readValueAsTree();
        String type = jsonNode.get("type").asText(); //NON-NLS
        return MediaType.valueOf(type);
    }
}
