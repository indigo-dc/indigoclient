package pl.psnc.indigo.fg.api.restful.jaxb.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;

import java.io.IOException;

/**
 * Deserializes information about input files from JSON objects.
 */
@SuppressWarnings("unchecked")
public final class InputFileDeserializer extends JsonDeserializer<InputFile> {
    @Override
    public InputFile deserialize(final JsonParser jsonParser, final
    DeserializationContext context) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        InputFile inputFile = new InputFile();

        if (!node.isObject()) {
            inputFile.setName(node.asText());
        }

        return inputFile;
    }
}
